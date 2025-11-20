package gr.opa.booking.common.requests;

import java.util.List;
import java.util.UUID;

import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.models.Room;

public class ClientSearchResults extends Message<List<Room>> {
    private UUID clientId;

    public ClientSearchResults() {

        setMessageType(MessageType.CLIENT_RESULTS);
    }

    public static ClientSearchResults from(SearchResults srr) {
        ClientSearchResults csrr = new ClientSearchResults();
        csrr.setData(srr.getData());
        csrr.setClientId(srr.getClientId());
        return csrr;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }
}
