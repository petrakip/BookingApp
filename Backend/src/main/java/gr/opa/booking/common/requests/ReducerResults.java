package gr.opa.booking.common.requests;

import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.models.Room;

import java.util.List;
import java.util.UUID;

public class ReducerResults extends Message<List<Room>> {
    private int totalWorkers;
    private int workerNumber;
    private UUID mapReduceId;
    private UUID clientId;

    public ReducerResults() {
        setMessageType(MessageType.REDUCER_RESULTS);
    }

    public UUID getMapReduceId() {
        return mapReduceId;
    }

    public void setMapReduceId(UUID mapReduceId) {
        this.mapReduceId = mapReduceId;
    }

    public int getTotalWorkers() {
        return totalWorkers;
    }

    public void setTotalWorkers(int totalWorkers) {
        this.totalWorkers = totalWorkers;
    }

    private void setWorkerNumber(int workerNumber) {
        this.workerNumber = workerNumber;
    }

    public int getWorkerNumber() {
        return workerNumber;
    }


    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public UUID getClientId() {
        return clientId;
    }

    public static ReducerResults from(WorkerSearch wsr) {
        ReducerResults rrr = new ReducerResults();
        rrr.setMapReduceId(wsr.getSearchId());
        rrr.setTotalWorkers(wsr.getTotalWorkers());
        rrr.setWorkerNumber(wsr.getWorkerNumber());
        rrr.setClientId(wsr.getClientId());
        return rrr;
    }

}
