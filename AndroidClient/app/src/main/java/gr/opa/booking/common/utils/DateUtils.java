package gr.opa.booking.common.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gr.opa.booking.common.models.Range;

public class DateUtils {
    public static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

    public static Date addDays(Date d, int days) {
        long currentTime = d.getTime();
        long addedInterval = DAY_IN_MILLIS * days;
        return new Date(currentTime + addedInterval);
    }

    public static List<Date> getDatesForRange(Range<Date> availableDates) {
        if (availableDates == null) return null;
        long delta = Math.abs(availableDates.getFrom().getTime() - availableDates.getTo().getTime());
        int days = (int) (delta / DAY_IN_MILLIS);
        List<Date> results = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            results.add(addDays(availableDates.getFrom(), i));
        }
        return results;
    }

    public static List<Date> getDatesForRange(Date from, Date to) {
        Range<Date> r = new Range<>();
        r.setFrom(from);
        r.setTo(to);
        return getDatesForRange(r);
    }

    public static Date today() {
        Date now = new Date();
        long remainder = now.getTime() % DAY_IN_MILLIS;
        return new Date(now.getTime() - remainder);
    }
}
