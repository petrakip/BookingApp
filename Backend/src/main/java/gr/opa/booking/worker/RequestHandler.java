package gr.opa.booking.worker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.opa.booking.common.TcpClient;
import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.models.Room;
import gr.opa.booking.common.models.RoomReservations;
import gr.opa.booking.common.models.RoomStatistics;
import gr.opa.booking.common.requests.*;

import java.io.IOException;
import java.util.List;

public class RequestHandler extends Thread {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Worker worker;
    private final String requestBody;

    private final TcpClient master;
    private final TcpClient reducer;

    public RequestHandler(Worker worker, String requestBody, TcpClient master, TcpClient reducer) {
        this.worker = worker;
        this.requestBody = requestBody;
        this.master = master;
        this.reducer = reducer;
    }

    @Override
    public void run() {
        try {
            System.out.println("Message received: " + requestBody);

            JsonNode requestJson = mapper.readTree(requestBody);
            MessageType messageType = MessageType.valueOf(
                    requestJson.findPath("messageType").textValue());

            System.out.printf("Received message with type [%s]%n", messageType);

            switch(messageType) {
                case SAVE_ROOM -> handleSavingRoom(requestJson);
                case WORKER_SEARCH -> handleWorkerSearch(requestJson);
                case WORKER_BOOKING_ROOM -> handleBookRoom(requestJson);
                case RATE_ROOM -> handleRateRoom(requestJson);
                case WORKER_STATISTICS -> handleWorkerStatistics(requestJson);
                case WORKER_RESERVATIONS ->  handleReservations(requestJson);
                default -> System.err.printf("Invalid message type detected: [%s]%n", messageType);
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    private void handleReservations(JsonNode requestJson) throws IOException {
        WorkerReservations wr = mapper.treeToValue(requestJson, WorkerReservations.class);
        System.out.println("WorkerReservations.reservationsId=" + wr.getReservationsId().toString());
        RoomReservations rr = worker.roomReservations(wr.getData());
        rr.setCriteria(wr.getData());
        ReducerReservations rrr = ReducerReservations.from(wr);
        System.out.println("ReducerReservations.reservationsId=" + rrr.getReservationsId().toString());
        rrr.setData(rr);
        reducer.sendRequest(rrr);
    }

    private void handleWorkerStatistics(JsonNode requestJson) throws IOException {
        WorkerStatistics ws = mapper.treeToValue(requestJson, WorkerStatistics.class);
        RoomStatistics rs = worker.roomStatistics(ws.getData());
        rs.setCriteria(ws.getData());
        ReducerStatistics rrs = ReducerStatistics.from(ws);
        rrs.setData(rs);
        reducer.sendRequest(rrs);
    }

    private void handleRateRoom(JsonNode requestJson) throws JsonProcessingException {
        RateRoom rr = mapper.treeToValue(requestJson, RateRoom.class);
        worker.addRoomRating(rr.getData().getRoomName(), rr.getData().getRating());
    }

    private void handleBookRoom(JsonNode requestTree) throws IOException {
        WorkerBookRoom wbr = mapper.treeToValue(requestTree, WorkerBookRoom.class);
        boolean success = worker.saveBooking(wbr.getData());
        if (success) {
            System.out.println("Room booked successfully");
            BookingSuccess bs = new BookingSuccess();
            bs.setData(wbr.getClientId());
            bs.setBookId(wbr.getBookId());
            master.sendRequest(bs);
        } else {
            System.out.println("Room could not be booked");
            BookingFailure bf = new BookingFailure();
            bf.setData(wbr.getClientId());
            bf.setBookId(wbr.getBookId());
            master.sendRequest(bf);
        }
    }

    private void handleWorkerSearch(JsonNode requestTree) throws IOException {
        WorkerSearch wsr = mapper.treeToValue(requestTree, WorkerSearch.class);
        List<Room> matchingRooms = worker.roomsSearch(wsr.getData());
        ReducerResults srr = ReducerResults.from(wsr);
        srr.setData(matchingRooms);
        reducer.sendRequest(srr);
    }

    private void handleSavingRoom(JsonNode requestTree) throws IOException {
        SaveRoom saveRoomRequest =
                mapper.treeToValue(requestTree, SaveRoom.class);
        worker.saveRoom(saveRoomRequest.getData());
        System.out.printf("Room [%s] saved%n", saveRoomRequest.getData().getRoomName());
    }
}
