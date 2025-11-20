package gr.opa.booking.common.requests;

import java.util.UUID;

import gr.opa.booking.common.enums.MessageType;

public class BookingSuccess extends Message<UUID> {
    public BookingSuccess() {
        setMessageType(MessageType.BOOKING_SUCCESS);
    }
}
