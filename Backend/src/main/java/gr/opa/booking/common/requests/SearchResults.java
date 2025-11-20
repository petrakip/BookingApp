package gr.opa.booking.common.requests;

import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.models.Room;

import java.util.List;
import java.util.UUID;

public class SearchResults extends Message<List<Room>> {
    private UUID clientId;

    public SearchResults() {
        setMessageType(MessageType.SEARCH_RESULTS);
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public UUID getClientId() {
        return clientId;
    }
}
