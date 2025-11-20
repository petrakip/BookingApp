package gr.opa.booking.common.requests;

import java.util.UUID;

import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.models.RoomStatistics;

public class ReducerStatistics extends Message<RoomStatistics> {
    private int totalWorkers;
    private UUID statisticsId;
    private UUID clientId;

    public ReducerStatistics() {
        setMessageType(MessageType.REDUCER_STATISTICS);
    }

    public static ReducerStatistics from(WorkerStatistics workerStatistics) {
        ReducerStatistics reducerStatistics = new ReducerStatistics();
        reducerStatistics.setStatisticsId(workerStatistics.getStatisticsId());
        reducerStatistics.setTotalWorkers(workerStatistics.getTotalWorkers());
        reducerStatistics.setClientId(workerStatistics.getClientId());
        return reducerStatistics;
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
}
