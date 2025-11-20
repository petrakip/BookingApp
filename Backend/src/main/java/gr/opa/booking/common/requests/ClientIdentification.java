package gr.opa.booking.common.requests;

import gr.opa.booking.common.enums.MessageType;

import java.util.UUID;

public class ClientIdentification extends Message<UUID> {
    public ClientIdentification() {
        setMessageType(MessageType.CLIENT_IDENTIFICATION);
    }
}
