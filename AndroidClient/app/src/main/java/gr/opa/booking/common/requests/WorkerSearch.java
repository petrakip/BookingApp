package gr.opa.booking.common.requests;

import java.util.UUID;

import gr.opa.booking.common.enums.MessageType;

public class WorkerSearch extends MasterSearch {
    private int totalWorkers;
    private UUID searchId;

    public WorkerSearch() {
        setMessageType(MessageType.WORKER_SEARCH);
    }

    public int getTotalWorkers() {
        return totalWorkers;
    }

    public void setTotalWorkers(int totalWorkers) {
        this.totalWorkers = totalWorkers;
    }

    public UUID getSearchId() {
        return searchId;
    }

    public void setSearchId(UUID mapReduceId) {
        this.searchId = mapReduceId;
    }

    public static WorkerSearch from(MasterSearch masterSearchRequest) {
        WorkerSearch wsr = new WorkerSearch();
        wsr.setData(masterSearchRequest.getData());
        wsr.setClientId(masterSearchRequest.getClientId());
        return wsr;
    }
}
