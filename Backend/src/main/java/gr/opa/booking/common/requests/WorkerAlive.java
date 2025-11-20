package gr.opa.booking.common.requests;

import gr.opa.booking.common.enums.MessageType;

public class WorkerAlive extends Message<Object> {
    public WorkerAlive() {
        setMessageType(MessageType.WORKER_ALIVE);
    }
}
