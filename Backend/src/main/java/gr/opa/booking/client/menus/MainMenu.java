package gr.opa.booking.client.menus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.opa.booking.common.TcpClient;
import gr.opa.booking.common.models.Range;
import gr.opa.booking.common.models.Room;
import gr.opa.booking.common.requests.AddRoom;
import gr.opa.booking.common.requests.ExitRequest;
import gr.opa.booking.common.utils.DateUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.random.RandomGenerator;

import static java.lang.System.in;
import static java.lang.System.out;

public class MainMenu implements Menu {
    private final TcpClient tcpClient;
    private final UUID clientId;

    public MainMenu(TcpClient tcpClient, UUID clientId) {
        this.tcpClient = tcpClient;
        this.clientId = clientId;
    }

    public void start() throws IOException, InterruptedException {
        blankLines(5);
        line();
        out.println("Booking System Client");
        help();
        prompt();

        Scanner s = new Scanner(in);

        int choice = s.nextInt();
        while(true) {
            switch (choice) {
                case 1 -> {
                    handleAddingRoom();
                    help();
                }
                case 2 -> handleRoomStatistics();
                case 3 -> handleRoomReservations();
                case 4 -> handleSearchAndBookRoom();
                case 5 -> handleRatingRoom();
                case 6 -> help();
                case 7 -> {
                    prepareExiting();
                    s.close();
                    out.println("Exiting...");
                    return;
                }
                default -> out.println("Invalid choice.");
            }

            prompt();
            choice = s.nextInt();
        }
    }

    private void blankLines(int count) {
        for (int i = 0; i < count; i++) out.println();
    }

    private void help() {
        line();
        out.println("Manager commands");
        out.println("----------------");
        out.println("1. Add new rooms (from JSON file)");
        out.println("2. Show room statistics");
        out.println("3. Show room reservations");
        line();
        out.println("Customer commands");
        out.println("-----------------");
        out.println("4. Search and book a room");
        out.println("5. Rate a room");
        line();
        out.println("Other commands");
        out.println("--------------");
        out.println("6. Help (print this menu)");
        out.println("7. Exit");
    }

    private void prompt() {
        line();
        out.print("[Main] Please enter choice: ");
    }

    private void line() {
        out.println("___________________________________________________");
    }

    private void handleSearchAndBookRoom() throws IOException {
        SearchMenu sm = new SearchMenu(tcpClient, clientId);
        sm.start();
        blankLines(2);
        help();
    }

    private void handleRatingRoom() throws IOException {
        RatingMenu rm = new RatingMenu(tcpClient);
        rm.start();
        blankLines(2);
        help();
    }

    private void handleRoomStatistics() throws IOException {
        StatisticsMenu sm = new StatisticsMenu(tcpClient, clientId);
        sm.start();
        blankLines(2);
        help();
    }

    private void handleRoomReservations() throws IOException, InterruptedException {
        ReservationsMenu rm = new ReservationsMenu(tcpClient, clientId);
        rm.start();
        blankLines(2);
        help();
    }


    private void handleAddingRoom() {
        List<Room> rooms = null;

        try {
            blankLines(3);
            System.out.println("Adding example rooms");
            blankLines(3);

            ObjectMapper mapper = new ObjectMapper();
            rooms = mapper.readValue(getClass().getResourceAsStream("/rooms.json"), new TypeReference<>() {});
        } catch (IOException e) {
            System.err.println(e);
        }

        if (rooms == null || rooms.isEmpty()) {
            System.err.println("Could not read rooms from JSON");
            return;
        }

        RandomGenerator random = RandomGenerator.of("Random");
        Date today = DateUtils.today();

        Date from = DateUtils.addDays(today, 0);
        Date to = DateUtils.addDays(today, 60);

        for(Room room : rooms) {
            Range<Date> availability = new Range<>();
            availability.setFrom(DateUtils.addDays(from, random.nextInt(-5, 5)));
            availability.setTo(DateUtils.addDays(to, random.nextInt(-5, 5)));
            room.setAvailableDates(availability);

            AddRoom roomRequest = new AddRoom();
            roomRequest.setData(room);
            try {
                tcpClient.sendRequest(roomRequest);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    private void prepareExiting() throws IOException {
        ExitRequest exiting = new ExitRequest();
        exiting.setData(tcpClient.getUuid());
        tcpClient.sendRequest(exiting);
        tcpClient.close();
    }
}
