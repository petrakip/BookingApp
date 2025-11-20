package gr.opa.booking.common.requests;

import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.models.Room;

public class AddRoom extends Message<Room> {
    public AddRoom() {
        setMessageType(MessageType.ADD_ROOM);
    }
}
