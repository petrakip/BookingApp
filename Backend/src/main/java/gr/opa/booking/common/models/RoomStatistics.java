package gr.opa.booking.common.models;

import java.util.HashMap;

public class RoomStatistics {
    private BookingStatisticsCriteria criteria;
    private HashMap<String, Integer> bookingsCountPerArea;

    public BookingStatisticsCriteria getCriteria() {
        return criteria;
    }

    public void setCriteria(BookingStatisticsCriteria criteria) {
        this.criteria = criteria;
    }

    public HashMap<String, Integer> getBookingsCountPerArea() {
        return bookingsCountPerArea;
    }

    public void setBookingsCountPerArea(HashMap<String, Integer> bookingsCountPerArea) {
        this.bookingsCountPerArea = bookingsCountPerArea;
    }
}
