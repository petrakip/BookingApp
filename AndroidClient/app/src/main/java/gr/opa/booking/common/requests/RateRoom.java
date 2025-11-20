package gr.opa.booking.common.requests;

import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.models.RoomRating;

public class RateRoom extends Message<RoomRating> {
    public RateRoom() {
        setMessageType(MessageType.RATE_ROOM);
    }
}
