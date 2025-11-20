package gr.opa.booking.common.requests;

import java.util.UUID;

import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.models.RoomReservationsCriteria;

public class ManagerReservations extends Message<RoomReservationsCriteria> {
    private UUID clientId;

    public ManagerReservations() {
        setMessageType(MessageType.ROOMS_RESERVATIONS);
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }
}
