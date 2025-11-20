package gr.opa.booking.common.requests;

import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.models.RoomStatistics;

import java.util.UUID;

public class StatisticsResults extends Message<RoomStatistics> {
    private UUID clientId;

    public StatisticsResults() {
        setMessageType(MessageType.STATISTICS_RESULTS);
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }
}
