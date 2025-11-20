package gr.opa.booking.client.menus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.opa.booking.common.TcpClient;
import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.models.Range;
import gr.opa.booking.common.models.SearchFilters;
import gr.opa.booking.common.requests.ClientSearchResults;
import gr.opa.booking.common.requests.MasterSearch;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

import static java.lang.System.in;
import static java.lang.System.out;

public class SearchMenu implements Menu {
    private final TcpClient tcpClient;
    private final UUID clientId;
    private SearchFilters searchFilters;

    public SearchMenu(TcpClient tcpClient, UUID clientId) {
        this.tcpClient = tcpClient;
        this.clientId = clientId;
    }

    @Override
    public void start() throws IOException {
        blankLines(5);
        line();
        out.println("Room search");
        printFilters();
        line();
        printFiltersMenu();
        prompt();

        Scanner scanner = new Scanner(in);

        int choice;
        while((choice = scanner.nextInt()) < 7) {
            switch(choice) {
                case 1 -> areaMenu(scanner);
                case 2 -> dateMenu(scanner);
                case 3 -> peopleMenu(scanner);
                case 4 -> priceMenu(scanner);
                case 5 -> ratingMenu(scanner);
                case 6 -> {
                    executeSearch();
                    return;
                }
                default -> out.println("Invalid choice.");
            }

            printFilters();
            prompt();
        }
    }

    private void prompt() {
        line();
        out.print("[Filters] Please enter choice: ");
    }

    private void executeSearch() throws IOException {
        MasterSearch sr = new MasterSearch();
        sr.setClientId(clientId);
        sr.setData(searchFilters);
        try {
            tcpClient.sendRequest(sr);

            String message;
            while((message = tcpClient.waitForMessage()) != null) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode responseJson = mapper.readTree(message);
                MessageType messageType = MessageType.valueOf(
                        responseJson.findPath("messageType").textValue());

                if (messageType == MessageType.CLIENT_RESULTS) {
                    ClientSearchResults csrr = mapper.readValue(message, ClientSearchResults.class);
                    BookMenu bookMenu = new BookMenu(csrr.getData(), tcpClient);
                    bookMenu.start();
                }
                break;
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }


    private void printFiltersMenu() {
        out.println("1. Enter area");
        out.println("2. Enter date ranges");
        out.println("3. Enter number of people");
        out.println("4. Enter price range");
        out.println("5. Enter rating range");
        line();
        out.println("6. Execute search");
        out.println("7. Cancel");
    }

    private void blankLines(@SuppressWarnings("SameParameterValue") int count) {
        for (int i = 0; i < count; i++) out.println();
    }

    private void ratingMenu(Scanner scanner) {
        if (searchFilters == null) searchFilters = new SearchFilters();
        if (searchFilters.getRatingRange() == null)
            searchFilters.setRatingRange(new Range<>());

        out.print("Please enter rating from: ");
        searchFilters.getRatingRange().setFrom(scanner.nextFloat());

        out.print("Please enter rating to: ");
        searchFilters.getRatingRange().setTo(scanner.nextFloat());
    }

    private void priceMenu(Scanner scanner) {
        if (searchFilters == null) searchFilters = new SearchFilters();
        if (searchFilters.getPriceRange() == null)
            searchFilters.setPriceRange(new Range<>());

        out.print("Please enter price from: ");
        searchFilters.getPriceRange().setFrom(scanner.nextBigDecimal());

        out.print("Please enter price to: ");
        searchFilters.getPriceRange().setTo(scanner.nextBigDecimal());
    }

    private void peopleMenu(Scanner scanner) {
        if (searchFilters == null) searchFilters = new SearchFilters();
        out.print("Please enter number of people: ");
        searchFilters.setNumberOfCustomers(scanner.nextInt());
    }

    private void dateMenu(Scanner scanner) {
        if (searchFilters == null) searchFilters = new SearchFilters();
        if (searchFilters.getDateRange() == null)
            searchFilters.setDateRange(new Range<>());

        Date from, to;
        String dateString;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");

        while(true) {
            try {
                out.print("Please enter date from (DD/MM/YY): ");
                dateString = scanner.next();
                from = df.parse(dateString);
                searchFilters.getDateRange().setFrom(from);
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
                searchFilters.getDateRange().setTo(to);
                break;
            } catch (Exception e) {
                out.print("Invalid date.");
            }
        }
    }

    private void areaMenu(Scanner scanner) {
        out.print("Please enter area (accepts wildcard '*'): ");
        if (searchFilters == null) searchFilters = new SearchFilters();
        searchFilters.setArea(scanner.next());
    }

    private void printFilters() {
        line();
        out.println("Current filters");
        out.println("---------------");

        if (searchFilters != null) {
            if (searchFilters.getArea() != null && !searchFilters.getArea().isEmpty()) {
                out.printf("\t✅ Area: %s%n", searchFilters.getArea());
            }

            if (searchFilters.getDateRange() != null) {
                out.printf("\t✅ Date from: %s to %s%n",
                        searchFilters.getDateRange().getFrom(),
                        searchFilters.getDateRange().getTo());
            }

            if (searchFilters.getNumberOfCustomers() != 0) {
                out.printf("\t✅ Number of people: %d%n", searchFilters.getNumberOfCustomers());
            }

            if (searchFilters.getPriceRange() != null) {
                out.printf("\t✅ Price from: %.02f to %.02f%n",
                        searchFilters.getPriceRange().getFrom(),
                        searchFilters.getPriceRange().getTo());
            }

            if (searchFilters.getRatingRange() != null) {
                out.printf("\t✅ Rating from: %.02f to %.02f%n",
                        searchFilters.getRatingRange().getFrom(),
                        searchFilters.getRatingRange().getTo());
            }
        } else {
            out.println("\t😢 No criteria specified");
        }
    }

    private void line() {
        out.println("___________________________________________________");
    }
}
