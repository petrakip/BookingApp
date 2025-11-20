package gr.opa.booking.master;

import gr.opa.booking.common.requests.ManagerReservations;
import gr.opa.booking.common.requests.WorkerReservations;

import java.util.UUID;

public class BroadcastReservations extends Thread {
    private final UUID reservationsId;
    private final Master master;
    private final ManagerReservations managerReservations;
    private int totalWorkers;

    public BroadcastReservations(Master master, ManagerReservations managerReservations) {
        this.master = master;
        this.managerReservations = managerReservations;
        this.reservationsId = UUID.randomUUID();
    }

    @Override
    public void run() {
        totalWorkers = master.getWorkersCount();
        master.forEachWorker(this::workerReservations);
    }

    private void workerReservations(WorkerFacade worker) {
        try {
            WorkerReservations ws = WorkerReservations.from(managerReservations);
            ws.setTotalWorkers(totalWorkers);
            ws.setReservationsId(reservationsId);
            ws.setWorkerNumber(worker.getWorkerNumber());
            System.out.println("BroadcastReservations.reservationsId=" + this.reservationsId.toString());

            worker.sendReservations(ws);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
