package gr.opa.booking.master;

import gr.opa.booking.common.requests.BookingStatistics;
import gr.opa.booking.common.requests.WorkerStatistics;

import java.util.UUID;

public class BroadcastStatistics extends Thread {
    private final UUID statisticsId;
    private final Master master;
    private final BookingStatistics bookingStatistics;
    private int totalWorkers;

    public BroadcastStatistics(Master master, BookingStatistics bookingStatistics) {
        this.master = master;
        this.bookingStatistics = bookingStatistics;
        this.statisticsId = UUID.randomUUID();
    }

    @Override
    public void run() {
        totalWorkers = master.getWorkersCount();
        master.forEachWorker(this::workerStatistics);
    }

    private void workerStatistics(WorkerFacade worker) {
        try {
            WorkerStatistics ws = WorkerStatistics.from(bookingStatistics);
            ws.setTotalWorkers(totalWorkers);
            ws.setStatisticsId(statisticsId);
            ws.setWorkerNumber(worker.getWorkerNumber());

            worker.sendStatistics(ws);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
