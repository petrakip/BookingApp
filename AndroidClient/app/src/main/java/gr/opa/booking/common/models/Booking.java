package gr.opa.booking.common.models;

import java.io.Serializable;
import java.util.Date;

public class Booking implements Serializable {
    private String name;
    private Date from;
    private Date to;
    private int people;
    private String roomName;

    public Booking() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "name='" + name + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", people=" + people +
                ", roomName='" + roomName + '\'' +
                '}';
    }
}
