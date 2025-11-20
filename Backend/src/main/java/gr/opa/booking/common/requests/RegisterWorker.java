package gr.opa.booking.common.requests;

import gr.opa.booking.common.enums.MessageType;

public class RegisterWorker extends Message<Object> {
    public RegisterWorker() {
        setMessageType(MessageType.REGISTER_WORKER);
    }
}
