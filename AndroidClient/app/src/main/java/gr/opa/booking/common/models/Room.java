package gr.opa.booking.common.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Room implements Serializable {
    private String roomName;
    private int numberOfPersons;
    private String area;
    private float rating;
    private int numberOfReviews;
    private String roomImage;
    private BigDecimal price;
    private Range<Date> availableDates;

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    private String managerName;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(int numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getNumberOfReviews() {
        return numberOfReviews;
    }

    public void setNumberOfReviews(int numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
    }

    public String getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Range<Date> getAvailableDates() {
        return availableDates;
    }

    public void setAvailableDates(Range<Date> availableDates) {
        this.availableDates = availableDates;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomName='" + roomName + '\'' +
                ", numberOfPersons=" + numberOfPersons +
                ", area='" + area + '\'' +
                ", rating=" + rating +
                ", numberOfReviews=" + numberOfReviews +
                ", roomImage='" + roomImage + '\'' +
                ", price=" + price +
                ", availableDates=" + availableDates +
                ", managerName='" + managerName + '\'' +
                '}';
    }
}