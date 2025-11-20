package gr.opa.booking.master;

import gr.opa.booking.common.TcpClient;
import gr.opa.booking.common.models.Room;
import gr.opa.booking.common.models.RoomRating;
import gr.opa.booking.common.requests.*;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Represents a united interface for dealing with a worker and its replicas.
 */
public class WorkerFacade {
    private ArrayList<TcpClient> replicas = new ArrayList<>();
    private TcpClient workerClient;
    private final Object workersLock = new Object();

    private final int numberOfReplicas;
    private final int workerNumber;

    public WorkerFacade(Socket socket, int numberOfReplicas, int workerNumber) throws IOException {
        this.workerClient = new TcpClient(socket);
        this.numberOfReplicas = numberOfReplicas;
        this.workerNumber = workerNumber;
    }

    public int getWorkerNumber() {
        return this.workerNumber;
    }

    public void setWorkerClient(Socket worker) throws IOException {
        this.workerClient = new TcpClient(worker);
    }

    private <T> T lockCleanThen(Supplier<T> supplier) {
        synchronized (workersLock) {
            workersCleanup();

            return supplier.get();
        }
    }

    private void workersCleanup() {
        ArrayList<TcpClient> newReplicas = new ArrayList<>();

        for(TcpClient replica : replicas) {
            if (!replica.isDead()) {
                newReplicas.add(replica);
            } else {
                System.err.println("Replica died - removing from list");
            }
        }

        replicas = newReplicas;

        if (workerClient.isDead()) {
            if (!replicas.isEmpty()) {
                System.err.println("Main worker died - promoting first replica to worker");
                workerClient.close();
                workerClient = replicas.remove(0);
            } else {
                System.err.println("No replica to promote - this set of workers is dead");
            }
        }
    }

    public boolean addReplica(Socket socket) {
        return lockCleanThen(() -> {
            try {
                if (workerClient == null) {
                    setWorkerClient(socket);
                    return true;
                }

                if (replicas.size() == numberOfReplicas) {
                    return false;
                } else {
                    replicas.add(new TcpClient(socket));
                    System.out.printf("Replica for worker %d added%n", workerNumber);
                    return true;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private <T extends Message<?>> void sendRequest(T request) {
        lockCleanThen(() -> {
            try {
                workerClient.sendRequest(request);
                for (TcpClient replica : replicas) {
                    replica.sendRequest(request);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return null;
        });
    }

    public void saveRoom(Room room) {
        SaveRoom saveRoomRequest = new SaveRoom();
        saveRoomRequest.setData(room);
        sendRequest(saveRoomRequest);
    }

    public void bookRoom(WorkerBookRoom request) {
        request.setBookId(UUID.randomUUID());
        sendRequest(request);
    }

    public void rateRoom(RoomRating room) {
        RateRoom rr = new RateRoom();
        rr.setData(room);
        sendRequest(rr);
    }

    public void sendStatistics(WorkerStatistics ws) {
        WorkerStatistics workerStatistics = new WorkerStatistics();
        workerStatistics.setData(ws.getData());
        workerStatistics.setTotalWorkers(ws.getTotalWorkers());
        workerStatistics.setWorkerNumber(ws.getWorkerNumber());
        workerStatistics.setClientId(ws.getClientId());
        workerStatistics.setStatisticsId(ws.getStatisticsId());
        sendRequest(workerStatistics);
    }

    public void sendSearch(WorkerSearch wsr) {
        WorkerSearch workerSearch = new WorkerSearch();
        workerSearch.setData(wsr.getData());
        workerSearch.setWorkerNumber(workerNumber);
        workerSearch.setTotalWorkers(wsr.getTotalWorkers());
        workerSearch.setClientId(wsr.getClientId());
        sendRequest(workerSearch);
    }

    public void alive(Socket clientSocket) {
        workerClient.alive(clientSocket);
        for (TcpClient replica : replicas) replica.alive(clientSocket);
    }

    public void sendReservations(WorkerReservations wr) {
        sendRequest(wr);
    }
}

