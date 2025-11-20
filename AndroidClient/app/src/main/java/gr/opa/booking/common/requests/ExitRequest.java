package gr.opa.booking.common.requests;

import java.util.UUID;

import gr.opa.booking.common.enums.MessageType;

public class ExitRequest extends Message<UUID> {
    public ExitRequest() {
        setMessageType(MessageType.CLIENT_EXIT);
    }
}
