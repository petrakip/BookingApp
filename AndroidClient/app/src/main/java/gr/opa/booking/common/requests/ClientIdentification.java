package gr.opa.booking.common.requests;

import java.util.UUID;

import gr.opa.booking.common.enums.MessageType;

public class ClientIdentification extends Message<UUID> {
    public ClientIdentification() {
        setMessageType(MessageType.CLIENT_IDENTIFICATION);
    }
}
