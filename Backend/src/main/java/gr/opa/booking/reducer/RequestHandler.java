package gr.opa.booking.reducer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.requests.ReducerReservations;
import gr.opa.booking.common.requests.ReducerResults;
import gr.opa.booking.common.requests.ReducerStatistics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RequestHandler extends Thread {
    private final Socket socket;
    private final Reducer reducer;
    private final PrintWriter out;
    private final BufferedReader in;
    private final ObjectMapper mapper;

    public RequestHandler(Socket socket, Reducer reducer) throws IOException {
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.socket = socket;
        this.reducer = reducer;
        this.mapper = new ObjectMapper();
    }

    @Override
    public void run() {
        try {
            String requestBody;
            while ((requestBody = in.readLine()) != null) {
                System.out.printf("Request received: %s%n", requestBody);

                JsonNode requestJson = mapper.readTree(requestBody);
                MessageType messageType = MessageType.valueOf(
                        requestJson.findPath("messageType").textValue());

                System.out.printf("Received message with type [%s]%n", messageType);

                switch (messageType) {
                    case REDUCER_RESULTS -> handleSearchResults(requestJson);
                    case REDUCER_STATISTICS -> handleReducerStatistics(requestJson);
                    case REDUCER_RESERVATIONS -> handleReducerReservations(requestJson);
                    default -> System.err.printf("Invalid message type detected: [%s]%n", messageType);
                }
            }

            in.close();
            out.close();
            socket.close();
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    private void handleReducerReservations(JsonNode requestJson) throws IOException {
        System.out.println("handleReducerReservations - before deser");
        ReducerReservations rr = mapper.treeToValue(requestJson, ReducerReservations.class);
        System.out.println("handleReducerReservations - after deser");
        reducer.reduceReservations(rr);
    }


    private void handleReducerStatistics(JsonNode requestJson) throws IOException {
        ReducerStatistics rs = mapper.treeToValue(requestJson, ReducerStatistics.class);
        reducer.reduceStatistics(rs);
    }

    private void handleSearchResults(JsonNode requestJson) throws IOException {
        ReducerResults rrr = mapper.treeToValue(requestJson, ReducerResults.class);
        reducer.reduceResults(rrr);
    }
}
