package gr.opa.booking.common.models;

public class RoomRating {
    private String roomName;
    private float rating;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "RoomRating{" +
                "roomName='" + roomName + '\'' +
                ", rating=" + rating +
                '}';
    }
}
