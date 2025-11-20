package gr.opa.booking.master;

import gr.opa.booking.common.TcpClient;
import gr.opa.booking.common.requests.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.function.Consumer;

public class Master {
    private final int port;
    private final int numberOfReplicas;

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final ArrayList<WorkerFacade> workers = new ArrayList<>();
    private final Object workersLock = new Object();

    private final Set<UUID> done = new HashSet<>();

    private HashMap<UUID, Socket> clients = new HashMap<>();
    private final Object clientsLock = new Object();
    private int workerNumber;

    public Master(int port, int numberOfReplicas) {
        this.port = port;
        this.numberOfReplicas = numberOfReplicas;
    }

    public void addWorker(Socket socket) throws IOException {
        synchronized (workersLock) {
            if (workers.isEmpty()) {
                workerNumber++;
                WorkerFacade worker = new WorkerFacade(socket, numberOfReplicas, workerNumber);
                workers.add(worker);
            } else {
                for (int i = 0; i < workers.size(); i++) {
                    if (workers.get(i).addReplica(socket)) return;
                }
                workerNumber++;
                WorkerFacade worker = new WorkerFacade(socket, numberOfReplicas, workerNumber);
                workers.add(worker);
            }
        }
    }


    public UUID getClientId(Socket clientSocket) {
        synchronized (clientsLock) {
            Set<UUID> keys = clients.keySet();
            for (UUID key : keys) {
                if (clients.get(key).equals(clientSocket))
                    return key;
            }
        }

        return null;
    }

    public WorkerFacade selectWorker(int hash) {
        synchronized (workersLock) {
            if (workers.isEmpty()) return null;
            return workers.get(Math.abs(hash) % workers.size());
        }
    }

    // Master is connected and is waiting clients to be connected
    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            //noinspection InfiniteLoopStatement
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                new RequestHandler(socket, this).start();
            }
        }
    }

    public void forEachWorker(Consumer<WorkerFacade> action) {
        synchronized (workersLock) {
            for (WorkerFacade worker : workers)
                action.accept(worker);
        }
    }

    // Here Master starts broadcast process to execute a searching request
    public void broadcastSearch(MasterSearch masterSearchRequest) {
        BroadcastSearch mp = new BroadcastSearch(masterSearchRequest, this);
        mp.start();
    }

    public int getWorkersCount() {
        synchronized (workersLock) {
            return workers.size();
        }
    }

    // clients are added to a HashMap (id, socket)
    // so that the master can return the results to the client that requested them
    public void addClient(UUID clientIdentifier, Socket socket) {
        synchronized (clientsLock) {
            if (!clients.containsKey(clientIdentifier)) {
                clients.put(clientIdentifier, socket);
            }
        }
    }

    // Master sends the results, that received from reducer, to client
    public void sendResultsToClient(ClientSearchResults csrr) throws IOException {
        synchronized (clientsLock) {
            if (clients.containsKey(csrr.getClientId())) {
                TcpClient tcpClient = new TcpClient(clients.get(csrr.getClientId()));
                tcpClient.sendRequest(csrr);
            }
        }
    }

    // Master sends message to Client if booking is successful
    public void sendBookingSuccessToClient(BookingSuccess bs) throws IOException {
        synchronized (clientsLock) {
            if (!done.contains(bs.getBookId())) {
                Socket clientSocket = clients.get(bs.getData());
                TcpClient client = new TcpClient(clientSocket);
                client.sendRequest(bs);
                done.add(bs.getBookId());
            }
        }
    }

    public void sendBookingFailureToClient(BookingFailure bf) throws IOException {
        synchronized (clientsLock) {
            if (!done.contains(bf.getBookId())) {
                Socket clientSocket = clients.get(bf.getData());
                TcpClient client = new TcpClient(clientSocket);
                client.sendRequest(bf);
                done.add(bf.getBookId());
            }
        }
    }

    // Here Master starts broadcast process to collect statistics
    public void broadcastStatistics(BookingStatistics bs) {
        BroadcastStatistics broadcastStatistics = new BroadcastStatistics(this, bs);
        broadcastStatistics.start();
    }

    // Master sends the statistics, that he received from workers to client
    public void sendStatisticsToClient(StatisticsResults statisticsResults) throws IOException {
        synchronized (clientsLock) {
            if (clients.containsKey(statisticsResults.getClientId())) {
                TcpClient tcpClient = new TcpClient(clients.get(statisticsResults.getClientId()));
                tcpClient.sendRequest(statisticsResults);
            }
        }
    }

    public void sendReservationsToClient(ReservationsResults reservationsResults) throws IOException {
        synchronized (clientsLock) {
            if (clients.containsKey(reservationsResults.getClientId())) {
                TcpClient tcpClient = new TcpClient(clients.get(reservationsResults.getClientId()));
                tcpClient.sendRequest(reservationsResults);
            }
        }
    }

    public UUID cleanDeadClient(UUID clientIdentifier) {
        for (int i = 0; i < clients.size(); i++) {
            if (clientIdentifier.equals(clients.get(clientIdentifier))) {
                synchronized (clientsLock) {
                    clients.remove(clientIdentifier);
                }
            }
        }
        return clientIdentifier;

    }

    public void workerAlive(Socket clientSocket) {
        for (WorkerFacade worker : workers) {
            worker.alive(clientSocket);
        }
    }

    public void broadcastReservations(ManagerReservations rr) {
        BroadcastReservations broadcastReservations = new BroadcastReservations(this, rr);
        broadcastReservations.start();
    }
}
