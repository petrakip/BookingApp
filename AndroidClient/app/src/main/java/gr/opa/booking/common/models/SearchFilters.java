package gr.opa.booking.common.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SearchFilters implements Serializable{
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
