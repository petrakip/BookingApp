package gr.opa.booking.master;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.requests.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class RequestHandler extends Thread {
    private final PrintWriter out;
    private final BufferedReader in;
    private final Socket clientSocket;
    private final Master master;
    private final ObjectMapper mapper;

    public RequestHandler(Socket clientSocket, Master master) throws IOException {
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.clientSocket = clientSocket;
        this.master = master;
        this.mapper = new ObjectMapper();
    }

    @Override
    public void run() {
        try {
            String requestBody;
            while((requestBody = in.readLine()) != null) {
                if (!requestBody.contains("WORKER_ALIVE"))
                    System.out.printf("Request received: %s%n", requestBody);

                JsonNode requestJson = mapper.readTree(requestBody);
                MessageType messageType = MessageType.valueOf(
                        requestJson.findPath("messageType").textValue());

                if (!messageType.equals(MessageType.WORKER_ALIVE))
                    System.out.printf("Received message with type [%s]%n", messageType);

                switch (messageType) {
                    case CLIENT_IDENTIFICATION -> handleClientIdentification(requestJson);
                    case ADD_ROOM -> handleAddRoomRequest(requestJson);
                    case REGISTER_WORKER -> handleRegisterWorkerRequest();
                    case MASTER_SEARCH -> handleSearch(requestJson);
                    case SEARCH_RESULTS -> handleSearchResults(requestJson);
                    case BOOKING_ROOM -> handleBookRoom(requestJson);
                    case BOOKING_SUCCESS -> handleBookingSuccess(requestJson);
                    case BOOKING_FAILURE -> handleBookingFailure(requestJson);
                    case RATE_ROOM -> handleRateRoom(requestJson);
                    case BOOKING_STATISTICS -> handleBookingStatistics(requestJson);
                    case STATISTICS_RESULTS -> handleStatisticsResults(requestJson);
                    case CLIENT_EXIT -> handleClientExit(requestJson);
                    case WORKER_ALIVE -> handleWorkerAlive();
                    case ROOMS_RESERVATIONS -> handleRoomReservation(requestJson);
                    case RESERVATIONS_RESULTS -> handleRoomReservationResults(requestJson);
                    default -> System.err.printf("Invalid message type detected: [%s]%n", messageType);
                }
            }

            in.close();
            out.close();
            clientSocket.close();
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    private void handleWorkerAlive() {
        master.workerAlive(this.clientSocket);
    }

    private void handleBookingFailure(JsonNode requestJson) throws IOException {
        BookingFailure bf = mapper.treeToValue(requestJson, BookingFailure.class);
        master.sendBookingFailureToClient(bf);
    }

    private void handleClientExit(JsonNode requestJson) throws JsonProcessingException {
        ExitRequest er = mapper.treeToValue(requestJson, ExitRequest.class);
        UUID uuid = master.cleanDeadClient(er.getData());
        System.out.printf("Client with [%s] had left. %n", uuid);
    }

    private void handleStatisticsResults(JsonNode requestJson) throws IOException {
        StatisticsResults sr = mapper.treeToValue(requestJson, StatisticsResults.class);
        master.sendStatisticsToClient(sr);
    }

    private void handleBookingStatistics(JsonNode requestJson) throws JsonProcessingException {
        BookingStatistics bs = mapper.treeToValue(requestJson, BookingStatistics.class);
        master.broadcastStatistics(bs);
    }

    private void handleRoomReservationResults(JsonNode requestJson) throws IOException {
        ReservationsResults rr = mapper.treeToValue(requestJson, ReservationsResults.class);
        master.sendReservationsToClient(rr);
    }

    private void handleRoomReservation(JsonNode requestJson) throws JsonProcessingException {
        ManagerReservations rr = mapper.treeToValue(requestJson, ManagerReservations.class);
        master.broadcastReservations(rr);
    }

    private void handleRateRoom(JsonNode requestJson) throws IOException {
        RateRoom rr = mapper.treeToValue(requestJson, RateRoom.class);
        WorkerFacade worker = master.selectWorker(rr.getData().getRoomName().hashCode());
        worker.rateRoom(rr.getData());
    }

    private void handleBookingSuccess(JsonNode requestJson) throws IOException {
        BookingSuccess bs = mapper.treeToValue(requestJson, BookingSuccess.class);
        master.sendBookingSuccessToClient(bs);
    }

    private void handleBookRoom(JsonNode requestJson) throws IOException {
        BookRoom brr = mapper.treeToValue(requestJson, BookRoom.class);
        WorkerBookRoom wbr = WorkerBookRoom.from(brr);
        wbr.setClientId(master.getClientId(clientSocket));
        WorkerFacade worker = master.selectWorker(brr.getData().getRoomName().hashCode());
        worker.bookRoom(wbr);
    }

    private void handleClientIdentification(JsonNode requestJson) throws JsonProcessingException {
        ClientIdentification cir = mapper.treeToValue(requestJson, ClientIdentification.class);
        master.addClient(cir.getData(), clientSocket);
    }

    private void handleSearchResults(JsonNode requestJson) throws IOException {
        SearchResults srr = mapper.treeToValue(requestJson, SearchResults.class);
        ClientSearchResults csrr = ClientSearchResults.from(srr);
        master.sendResultsToClient(csrr);
    }

    private void handleSearch(JsonNode requestJson) throws JsonProcessingException {
        MasterSearch masterSearchRequest = mapper.treeToValue(requestJson, MasterSearch.class);
        master.broadcastSearch(masterSearchRequest);
    }

    private void handleRegisterWorkerRequest() throws IOException {
        System.out.println("New worker registered");
        master.addWorker(this.clientSocket);
    }

    private void handleAddRoomRequest(JsonNode requestTree) throws IOException {
        AddRoom addRoomRequest = mapper.treeToValue(requestTree, AddRoom.class);
        WorkerFacade worker = master.selectWorker(addRoomRequest.getData().getRoomName().hashCode());

        if (worker == null) {
            System.out.println("Cannot add room - no workers found");
            return;
        }

        worker.saveRoom(addRoomRequest.getData());
    }
}
