package gr.opa.booking.master;

import gr.opa.booking.common.requests.MasterSearch;
import gr.opa.booking.common.requests.WorkerSearch;

import java.util.UUID;

public class BroadcastSearch extends Thread {
    // This should be called just "id", but we don't want to obfuscate the Thread.id
    private final UUID searchId;
    private final MasterSearch masterSearchRequest;
    private final Master master;
    private int totalWorkers;

    public BroadcastSearch(MasterSearch masterSearchRequest, Master master) {
        this.masterSearchRequest = masterSearchRequest;
        this.master = master;
        this.searchId = UUID.randomUUID();
    }

    @Override
    public void run() {
        totalWorkers = master.getWorkersCount();
        master.forEachWorker(this::workerSearch);
    }

    private void workerSearch(WorkerFacade worker) {
        try {
            WorkerSearch wsr = WorkerSearch.from(masterSearchRequest);
            wsr.setTotalWorkers(this.totalWorkers);
            wsr.setWorkerNumber(worker.getWorkerNumber());
            wsr.setSearchId(searchId);
            worker.sendSearch(wsr);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
