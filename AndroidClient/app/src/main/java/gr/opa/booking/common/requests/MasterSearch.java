package gr.opa.booking.common.requests;

import java.util.UUID;

import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.models.SearchFilters;

public class MasterSearch extends Message<SearchFilters> {
    private UUID clientId;

    public MasterSearch() {
        setMessageType(MessageType.MASTER_SEARCH);
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public UUID getClientId() {
        return clientId;
    }
}
