package gr.opa.booking.client.menus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.opa.booking.common.TcpClient;
import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.models.BookingStatisticsCriteria;
import gr.opa.booking.common.models.RoomStatistics;
import gr.opa.booking.common.requests.BookingStatistics;
import gr.opa.booking.common.requests.StatisticsResults;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

import static java.lang.System.in;
import static java.lang.System.out;

public class StatisticsMenu implements Menu {
    private final TcpClient tcpClient;
    private final UUID clientId;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");

    public StatisticsMenu(TcpClient tcpClient, UUID clientId) {
        this.tcpClient = tcpClient;
        this.clientId = clientId;
    }

    @Override
    public void start() throws IOException {
        blankLines(5);
        line();
        out.println("Statistics");
        line();
        Date from, to;
        String dateString;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");

        Scanner scanner = new Scanner(in);

        while(true) {
            try {
                out.print("Please enter date from (DD/MM/YY): ");
                dateString = scanner.next();
                from = df.parse(dateString);
                break;
            } catch (Exception e) {
                out.print("Invalid date.");
                return;
            }
        }

        while(true) {
            try {
                out.print("Please enter date to (DD/MM/YY): ");
                dateString = scanner.next();
                to = df.parse(dateString);
                break;
            } catch (Exception e) {
                out.print("Invalid date.");
            }
        }

        BookingStatisticsCriteria criteria = new BookingStatisticsCriteria();
        criteria.setBookingPeriodFrom(from);
        criteria.setBookingPeriodTo(to);
        BookingStatistics bs = new BookingStatistics();
        bs.setClientId(clientId);
        bs.setData(criteria);
        tcpClient.sendRequest(bs);

        try
        {
            String response;
            while((response = tcpClient.waitForMessage()) != null) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode responseJson = mapper.readTree(response);
                MessageType messageType = MessageType.valueOf(
                        responseJson.findPath("messageType").textValue());

                if (messageType == MessageType.STATISTICS_RESULTS) {
                    StatisticsResults rs = mapper.treeToValue(responseJson, StatisticsResults.class);
                    RoomStatistics roomStats = rs.getData();
                    out.println();
                    out.printf("Statistics for period: [%s] to [%s]%n",
                            dateFormat.format(roomStats.getCriteria().getBookingPeriodFrom()),
                            dateFormat.format(roomStats.getCriteria().getBookingPeriodTo()));
                    line();

                    for (String key : roomStats.getBookingsCountPerArea().keySet()) {
                        out.printf("%s: %d%n", key, roomStats.getBookingsCountPerArea().get(key));
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
