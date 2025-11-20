package gr.opa.booking.common.requests;

import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.models.BookingStatisticsCriteria;

import java.util.UUID;

public class WorkerStatistics extends Message<BookingStatisticsCriteria> {
    public int workerNumber;
    private int totalWorkers;
    private UUID statisticsId;
    private UUID clientId;

    public WorkerStatistics() {
        setMessageType(MessageType.WORKER_STATISTICS);
    }

    public static WorkerStatistics from(BookingStatistics bookingStatistics) {
        WorkerStatistics result = new WorkerStatistics();
        result.setData(bookingStatistics.getData());
        result.setClientId(bookingStatistics.getClientId());
        return result;
    }

    public int getTotalWorkers() {
        return totalWorkers;
    }

    public void setTotalWorkers(int totalWorkers) {
        this.totalWorkers = totalWorkers;
    }

    public UUID getStatisticsId() {
        return statisticsId;
    }

    public void setStatisticsId(UUID statisticsId) {
        this.statisticsId = statisticsId;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public void setWorkerNumber(int workerNumber) {
        this.workerNumber = workerNumber;
    }

    public int getWorkerNumber() {
        return workerNumber;
    }
}
