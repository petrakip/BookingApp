package gr.opa.booking.common.requests;

import java.util.UUID;

import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.models.BookingStatisticsCriteria;

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
