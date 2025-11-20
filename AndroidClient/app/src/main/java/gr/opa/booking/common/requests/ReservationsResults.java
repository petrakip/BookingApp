package gr.opa.booking.common.requests;

import java.util.UUID;

import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.models.RoomReservations;

public class ReservationsResults extends Message<RoomReservations> {
    private UUID clientId;

    public ReservationsResults() {
        setMessageType(MessageType.RESERVATIONS_RESULTS);
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }
}
