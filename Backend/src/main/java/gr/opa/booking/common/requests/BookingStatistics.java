package gr.opa.booking.common.requests;

import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.models.BookingStatisticsCriteria;

import java.util.UUID;

public class BookingStatistics extends Message<BookingStatisticsCriteria> {
    private UUID clientId;

    public BookingStatistics() {
        setMessageType(MessageType.BOOKING_STATISTICS);
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }
}
