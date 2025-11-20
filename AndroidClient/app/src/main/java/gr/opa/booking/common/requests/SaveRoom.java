package gr.opa.booking.common.requests;

import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.models.Room;

public class SaveRoom extends Message<Room> {
    public SaveRoom() {
        setMessageType(MessageType.SAVE_ROOM);
    }
}
