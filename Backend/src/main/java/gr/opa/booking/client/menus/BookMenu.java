package gr.opa.booking.client.menus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.opa.booking.common.TcpClient;
import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.models.Booking;
import gr.opa.booking.common.models.Room;
import gr.opa.booking.common.requests.BookRoom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

public class BookMenu implements Menu {
    private final List<Room> rooms;
    private final TcpClient tcpClient;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");

    public BookMenu(List<Room> rooms, TcpClient tcpClient) {
        this.rooms = rooms;
        this.tcpClient = tcpClient;
    }

    private void line() {
        out.println("___________________________________________________");
    }

    private void blankLines(@SuppressWarnings("SameParameterValue") int count) {
        for (int i = 0; i < count; i++) out.println();
    }

    @Override
    public void start() {
        blankLines(5);
        line();
        out.println("Book a room");
        printRooms();
        prompt();
        Scanner scanner = new Scanner(in);

        int choice;
        while((choice = scanner.nextInt()) != 0) {
            while (true) {
                int index = choice -1;
                if (index >= rooms.size()) {
                    out.println("Invalid choice.");
                    prompt();
                    continue;
                }
                Room room = rooms.get(index);
                if (getDataAndSendBooking(scanner, room)) return;
                 break;

            }
            out.println();
            printRooms();
            out.println();
            prompt();
            out.println();

        }
    }

    private boolean getDataAndSendBooking(Scanner scanner, Room room) {
        Date from, to;
        int people;

        out.print("Enter name for the booking: ");
        String name = scanner.next();

        while(true) {
            try {
                out.print("Please enter the start date of the booking (dd/MM/yy): ");
                String fm = scanner.next();
                from = dateFormat.parse(fm);
                break;
            } catch (ParseException ignored) {
                out.println("Invalid date. Please use the format dd/MM/yy (ie. 01/01/24).");
            }
        }

        while(true) {
            try {
                out.print("Please enter the end date of the booking (dd/MM/yy): ");
                String t = scanner.next();
                to = dateFormat.parse(t);
                break;
            } catch (ParseException ignored) {
                out.println("Invalid date. Please use the format dd/MM/yy (ie. 01/01/24).");
            }
        }

        out.print("Enter number of people: ");
        people = scanner.nextInt();
        return sendBooking(name, from, to, people, room);
    }

    private boolean sendBooking(String name, Date from, Date to, int people, Room room) {
        out.println("Booking room...");
        Booking b = new Booking();
        b.setName(name);
        b.setFrom(from);
        b.setTo(to);
        b.setPeople(people);
        b.setRoomName(room.getRoomName());
        BookRoom bookRoomRequest = new BookRoom();
        bookRoomRequest.setData(b);

        try {
            System.out.println("Sending booking request");
            tcpClient.sendRequest(bookRoomRequest);
            System.out.println("Waiting for response");

            String response;
            while((response = tcpClient.waitForMessage()) != null) {
                // debug:
                out.println("Received response: " + response);

                ObjectMapper mapper = new ObjectMapper();
                JsonNode responseJson = mapper.readTree(response);
                MessageType messageType = MessageType.valueOf(
                        responseJson.findPath("messageType").textValue());

                if (messageType.equals(MessageType.BOOKING_FAILURE)) {
                    out.println();
                    out.println("Failed to book the room. Please try again later.");
                    out.println();
                    return false;
                }

                if (messageType.equals(MessageType.BOOKING_SUCCESS)) {
                    out.println();
                    out.println("The room was booked successfully!");
                    out.println();
                    return true;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    private void printRooms() {
        if (rooms.size() == 0) {
            out.println("No rooms found.");
            out.println("0. Back");
            return;
        }

        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            String from = dateFormat.format(room.getAvailableDates().getFrom());
            String to = dateFormat.format(room.getAvailableDates().getTo());

            out.printf("%d. %s, available: [%s - %s], people: %d, stars: %.02f, price: %.02f%n",
                    i+1, room.getRoomName(), from, to, room.getNumberOfPersons(), room.getRating(), room.getPrice());
        }
        out.println("0. Back");
    }

    private void prompt() {
        out.print("[Book] Enter room number or 0: ");
    }
}
