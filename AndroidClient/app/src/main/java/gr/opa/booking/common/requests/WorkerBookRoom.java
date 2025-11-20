package gr.opa.booking.common.requests;

import java.util.UUID;

import gr.opa.booking.common.enums.MessageType;

public class WorkerBookRoom extends BookRoom {
    private UUID clientId;

    public WorkerBookRoom() {
        setMessageType(MessageType.WORKER_BOOKING_ROOM);
    }

    public static WorkerBookRoom from(BookRoom bookRoom) {
        WorkerBookRoom wbr = new WorkerBookRoom();
        wbr.setData(bookRoom.getData());
        return wbr;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }
}
