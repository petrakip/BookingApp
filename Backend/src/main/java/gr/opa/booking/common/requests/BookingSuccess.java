package gr.opa.booking.common.requests;

import gr.opa.booking.common.enums.MessageType;

import java.util.UUID;

public class BookingSuccess extends Message<UUID> {
    private UUID bookId;

    public BookingSuccess() {
        setMessageType(MessageType.BOOKING_SUCCESS);
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }
}
