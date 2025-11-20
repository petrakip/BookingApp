package gr.opa.booking.common.requests;

import gr.opa.booking.common.enums.MessageType;

import java.util.UUID;

public class WorkerBookRoom extends BookRoom {
    private UUID clientId;
    private UUID bookId;

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

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }
}
