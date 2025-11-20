package gr.opa.booking.common.requests;

import gr.opa.booking.common.enums.MessageType;

import java.util.UUID;

public class ExitRequest extends Message<UUID> {
    public ExitRequest() {
        setMessageType(MessageType.CLIENT_EXIT);
    }
}
