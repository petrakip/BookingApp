package gr.opa.booking.common.requests;

import gr.opa.booking.common.enums.MessageType;

import java.util.UUID;

public class BookingFailure extends Message<UUID> {
    private UUID bookId;

    public BookingFailure() {
        setMessageType(MessageType.BOOKING_FAILURE);
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }
}
