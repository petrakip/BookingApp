package gr.opa.booking.common.models;

import java.io.Serializable;
import java.util.Date;

public class BookingStatisticsCriteria implements Serializable {
    private Date bookingPeriodFrom;
    private Date bookingPeriodTo;

    public Date getBookingPeriodTo() {
        return bookingPeriodTo;
    }

    public void setBookingPeriodTo(Date bookingPeriodTo) {
        this.bookingPeriodTo = bookingPeriodTo;
    }

    public Date getBookingPeriodFrom() {
        return bookingPeriodFrom;
    }

    public void setBookingPeriodFrom(Date bookingPeriodFrom) {
        this.bookingPeriodFrom = bookingPeriodFrom;
    }
}
