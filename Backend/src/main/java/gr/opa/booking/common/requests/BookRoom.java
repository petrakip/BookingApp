package gr.opa.booking.common.requests;

import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.models.Booking;

public class BookRoom extends Message<Booking> {
    public BookRoom() {
        setMessageType(MessageType.BOOKING_ROOM);
    }
}
