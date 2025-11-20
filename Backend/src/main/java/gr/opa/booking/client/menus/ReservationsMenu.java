package gr.opa.booking.client.menus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.opa.booking.common.TcpClient;
import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.models.Booking;
import gr.opa.booking.common.models.RoomReservations;
import gr.opa.booking.common.models.RoomReservationsCriteria;
import gr.opa.booking.common.requests.ManagerReservations;
import gr.opa.booking.common.requests.ReservationsResults;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import static java.lang.System.in;
import static java.lang.System.out;

public class ReservationsMenu implements Menu{
    private final TcpClient tcpClient;
    private final UUID clientId;

    public ReservationsMenu(TcpClient tcpClient, UUID clientId) {
        this.tcpClient = tcpClient;
        this.clientId = clientId;
    }

    @Override
    public void start() throws IOException, InterruptedException {
        blankLines(5);
        line();
        out.println("Reservations");
        line();
        String name;

        Scanner scanner = new Scanner(in);

        while(true) {
            try {
                out.print("Please enter name: ");
                name = scanner.next();
                break;
            }catch (Exception e) {
                out.print("Invalid name.");
                return;
            }
        }

        RoomReservationsCriteria criteria = new RoomReservationsCriteria();
        criteria.setManagerName(name);
        ManagerReservations mr = new ManagerReservations();
        mr.setClientId(clientId);
        mr.setData(criteria);
        tcpClient.sendRequest(mr);

        try
        {
            String response;
            while((response = tcpClient.waitForMessage()) != null) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode responseJson = mapper.readTree(response);
                MessageType messageType = MessageType.valueOf(
                        responseJson.findPath("messageType").textValue());

                if (messageType == MessageType.RESERVATIONS_RESULTS) {
                    ReservationsResults rr = mapper.treeToValue(responseJson, ReservationsResults.class);
                    RoomReservations roomReservations = rr.getData();
                    out.println();
                    out.printf("Reservations for  [%s]%n", roomReservations.getCriteria().getManagerName());
                    line();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
                    List<Booking> bookings = new ArrayList<>();
                    for (String roomName : roomReservations.getReservations().keySet()) {
                        bookings.addAll(roomReservations.getReservations().get(roomName));
                    }
                     for (Booking book : bookings) {
                       out.printf("%s: Customer: %s Date[from]: %s Date[to]: %s People: %d %n", book.getRoomName(), book.getName(), dateFormat.format(book.getFrom()),
                               dateFormat.format(book.getTo()), book.getPeople());
                    }

                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void line() {
        out.println("___________________________________________________");
    }

    private void blankLines(int count) {
        for (int i = 0; i < count; i++) out.println();
    }
}

