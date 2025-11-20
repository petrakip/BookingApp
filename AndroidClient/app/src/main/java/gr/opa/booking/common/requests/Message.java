package gr.opa.booking.common.requests;

import gr.opa.booking.common.enums.MessageType;

public abstract class Message<T> {
    private T data;

    private MessageType messageType;

    protected final void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public final MessageType getMessageType() {
        return messageType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
