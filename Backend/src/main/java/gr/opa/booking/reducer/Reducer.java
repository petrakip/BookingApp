package gr.opa.booking.reducer;

import gr.opa.booking.common.TcpClient;
import gr.opa.booking.common.models.Room;
import gr.opa.booking.common.models.RoomReservations;
import gr.opa.booking.common.models.RoomStatistics;
import gr.opa.booking.common.requests.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Reducer {
    private final int port;
    private final String masterHost;
    private final int masterPort;
    private final Object reduceResultsLock = new Object();
    private final HashMap<UUID, ReduceResults> reduceResultsOperations = new HashMap<>();

    private final Object reduceStatisticsLock = new Object();
    private final HashMap<UUID, ReduceStatistics> reduceStatisticsOperations =
            new HashMap<>();

    private final Object reduceReservationsLock = new Object();
    private final HashMap<UUID, ReduceReservations> reduceReservationsOperations =
            new HashMap<>();

    private TcpClient master;

    public Reducer(int port, String masterHost, int masterPort) {
        this.port = port;
        this.masterHost = masterHost;
        this.masterPort = masterPort;
    }

    public void start() throws IOException {
        master = new TcpClient(masterHost, masterPort);
        master.connect();
        System.out.printf("Connected to master [%s:%d]%n", masterHost, masterPort);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            //noinspection InfiniteLoopStatement
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                new RequestHandler(socket, this).start();
            }
        }
    }

    public void reduceResults(ReducerResults rrr) throws IOException {
        ReduceResults targetOperation ;

        synchronized (reduceResultsLock) {
            if (!reduceResultsOperations.containsKey(rrr.getMapReduceId())) {
                reduceResultsOperations.put(rrr.getMapReduceId(), new ReduceResults(rrr.getTotalWorkers()));
            }

            targetOperation = reduceResultsOperations.get(rrr.getMapReduceId());
            targetOperation.addResults(rrr.getData(), rrr.getWorkerNumber());

            if (targetOperation.resultsCollected()) {
                List<Room> results = targetOperation.getResults();

                SearchResults searchResultsRequest = new SearchResults();
                searchResultsRequest.setData(results);
                searchResultsRequest.setClientId(rrr.getClientId());
                master.sendRequest(searchResultsRequest);
                targetOperation.clearWorkersDone();

                reduceResultsOperations.remove(rrr.getMapReduceId());
            }
            targetOperation.clearResults();
        }
    }

    public void reduceStatistics(ReducerStatistics rs) throws IOException {
        ReduceStatistics targetOperation;

        synchronized (reduceStatisticsLock) {
            if (!reduceStatisticsOperations.containsKey(rs.getStatisticsId())) {
                reduceStatisticsOperations.put(rs.getStatisticsId(), new ReduceStatistics(rs.getTotalWorkers()));
            }

            targetOperation = reduceStatisticsOperations.get(rs.getStatisticsId());

            targetOperation.addStats(rs.getData(), rs.getWorkerNumber());

            if (targetOperation.statsCollected()) {
                RoomStatistics roomStatistics = targetOperation.getResults();

                StatisticsResults sr = new StatisticsResults();
                sr.setData(roomStatistics);
                sr.setClientId(rs.getClientId());
                master.sendRequest(sr);

                targetOperation.clearStatistics();
                reduceStatisticsOperations.remove(rs.getStatisticsId());
            }
        }
    }

    public void reduceReservations(ReducerReservations rr) throws IOException {
        ReduceReservations targetOperation;

        synchronized (reduceReservationsLock) {
            if (!reduceReservationsOperations.containsKey((rr.getReservationsId()))) {
                System.out.println("Started tracking this reservation: " + rr.getReservationsId().toString());
                reduceReservationsOperations.put(rr.getReservationsId(), new ReduceReservations(rr.getTotalWorkers()));
            }

            targetOperation = reduceReservationsOperations.get(rr.getReservationsId());

            System.out.println("Adding to this reservation: " + rr.getReservationsId().toString());
            targetOperation.addReservations(rr.getData(), rr.getWorkerNumber());

            if (targetOperation.reservationsCollected()) {
                System.out.println("Reservations collected");

                RoomReservations roomReservations = targetOperation.getResults();

                ReservationsResults rResults = new ReservationsResults();
                rResults.setData(roomReservations);
                rResults.setClientId(rr.getClientId());
                master.sendRequest(rResults);

                reduceReservationsOperations.remove(rr.getReservationsId());
            }
        }
    }
}
