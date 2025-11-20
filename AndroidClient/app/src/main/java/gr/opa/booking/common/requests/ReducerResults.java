package gr.opa.booking.common.requests;

import java.util.List;
import java.util.UUID;

import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.models.Room;

public class ReducerResults extends Message<List<Room>> {
    private int totalWorkers;
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

    public static ReducerResults from(WorkerSearch wsr) {
        ReducerResults rrr = new ReducerResults();
        rrr.setMapReduceId(wsr.getSearchId());
        rrr.setTotalWorkers(wsr.getTotalWorkers());
        rrr.setClientId(wsr.getClientId());
        return rrr;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public UUID getClientId() {
        return clientId;
    }
}
