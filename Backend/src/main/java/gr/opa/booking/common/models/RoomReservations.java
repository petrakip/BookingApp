package gr.opa.booking.common.models;

import java.util.List;
import java.util.Map;

public class RoomReservations {
    RoomReservationsCriteria criteria;

    private Map<String, List<Booking>> reservations;

    public RoomReservationsCriteria getCriteria() {
        return criteria;
    }

    public void setCriteria(RoomReservationsCriteria criteria) {
        this.criteria = criteria;
    }

    public Map<String, List<Booking>> getReservations() {
        return reservations;
    }

    public void setReservations(Map<String, List<Booking>> reservations) {
        this.reservations = reservations;
    }
}
