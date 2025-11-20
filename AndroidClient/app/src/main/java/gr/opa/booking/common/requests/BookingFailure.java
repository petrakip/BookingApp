package gr.opa.booking.common.requests;

import java.util.UUID;

import gr.opa.booking.common.enums.MessageType;

public class BookingFailure extends Message<UUID> {
    public BookingFailure() {
        setMessageType(MessageType.BOOKING_FAILURE);
    }
}
