package gr.opa.booking.client.menus;

import gr.opa.booking.common.TcpClient;
import gr.opa.booking.common.models.RoomRating;
import gr.opa.booking.common.requests.RateRoom;

import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

public class RatingMenu implements Menu {
    private final TcpClient tcpClient;
    private String roomName;
    private float rating;

    public RatingMenu(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    @Override
    public void start() throws IOException {
        blankLines(5);
        line();
        out.println("Rate a room");
        line();
        out.print("[Rate] Please enter room name: ");
        Scanner scanner = new Scanner(in);
        roomName = scanner.next();

        while(true) {
            out.print("[Rate] Please enter room rating [1.00 - 5.00]:");
            rating = scanner.nextFloat();
            if (rating <= 0 || rating >= 5) {
                out.println("Invalid value for rating. Should be between 1.00 and 5.00.");
                continue;
            }
            break;
        }
        RoomRating ratingModel = new RoomRating();
        ratingModel.setRating(rating);
        ratingModel.setRoomName(roomName);
        RateRoom rr = new RateRoom();
        rr.setData(ratingModel);
        tcpClient.sendRequest(rr);
        out.println("Room rating has been entered.");
    }

    private void line() {
        out.println("___________________________________________________");
    }

    private void blankLines(int count) {
        for (int i = 0; i < count; i++) out.println();
    }
}
