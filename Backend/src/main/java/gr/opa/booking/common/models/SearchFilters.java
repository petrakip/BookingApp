package gr.opa.booking.common.models;

import gr.opa.booking.common.utils.DateUtils;
import gr.opa.booking.worker.Worker;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class SearchFilters {
    private String area;
    private Range<Date> dateRange;
    private int numberOfCustomers;
    private Range<BigDecimal> priceRange;
    private Range<Float> ratingRange;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Range<Date> getDateRange() {
        return dateRange;
    }

    public void setDateRange(Range<Date> dateRange) { this.dateRange = dateRange; }

    public Range<Float> getRatingRange() {
        return ratingRange;
    }

    public void setRatingRange(Range<Float> ratingRange) {
        this.ratingRange = ratingRange;
    }

    public Range<BigDecimal> getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(Range<BigDecimal> priceRange) {
        this.priceRange = priceRange;
    }

    public int getNumberOfCustomers() {
        return numberOfCustomers;
    }

    public void setNumberOfCustomers(int numberOfCustomers) {
        this.numberOfCustomers = numberOfCustomers;
    }

    public boolean Match(Room room, Worker worker) {
        return areaMatch(room)
                && dateRangeMatch(room, worker)
                && ratingRangeMatch(room)
                && priceRangeMatch(room)
                && numberOfCustomersMatch(room);
    }

    private boolean numberOfCustomersMatch(Room room) {
        if (numberOfCustomers <= 0)  return true;

        return numberOfCustomers == room.getNumberOfPersons();
    }

    private boolean priceRangeMatch(Room room) {
        if (priceRange == null) return true;

        return priceRange.getFrom().compareTo(room.getPrice()) <= 0
                && priceRange.getTo().compareTo(room.getPrice()) >= 0;
    }

    private boolean ratingRangeMatch(Room room) {
        if (ratingRange == null) return true;

        return ratingRange.getFrom().compareTo(room.getRating()) <= 0
                && ratingRange.getTo().compareTo(room.getRating()) >= 0;
    }

    private boolean dateRangeMatch(Room room, Worker worker) {
        if (dateRange == null) return true;
        if (room.getAvailableDates() == null) return false;

        List<Date> dateCriteria = DateUtils.getDatesForRange(dateRange);
        return worker.isAvailableForDates(room, dateCriteria);
    }

    private boolean areaMatch(Room room) {
        if (area == null || area.isBlank()) return true;

        String areaPatternString = area.replace("*", ".+");
        Pattern areaPattern = Pattern.compile(areaPatternString, Pattern.CASE_INSENSITIVE);
        return areaPattern.matcher(room.getArea()).find();
    }

    @Override
    public String toString() {
        return "SearchFilters{" +
                "area='" + area + '\'' +
                ", dateRange=" + dateRange +
                ", numberOfCustomers=" + numberOfCustomers +
                ", priceRange=" + priceRange +
                ", ratingRange=" + ratingRange +
                '}';
    }
}
