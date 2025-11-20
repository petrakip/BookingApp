package gr.opa.booking.android;

import static java.lang.System.out;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import gr.opa.booking.common.TcpClient;
import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.models.Booking;
import gr.opa.booking.common.models.BookingStatisticsCriteria;
import gr.opa.booking.common.models.Range;
import gr.opa.booking.common.models.Room;
import gr.opa.booking.common.models.RoomRating;
import gr.opa.booking.common.models.RoomReservations;
import gr.opa.booking.common.models.RoomReservationsCriteria;
import gr.opa.booking.common.models.RoomStatistics;
import gr.opa.booking.common.models.SearchFilters;
import gr.opa.booking.common.requests.AddRoom;
import gr.opa.booking.common.requests.BookRoom;
import gr.opa.booking.common.requests.BookingStatistics;
import gr.opa.booking.common.requests.ClientIdentification;
import gr.opa.booking.common.requests.ClientSearchResults;
import gr.opa.booking.common.requests.ManagerReservations;
import gr.opa.booking.common.requests.MasterSearch;
import gr.opa.booking.common.requests.RateRoom;
import gr.opa.booking.common.requests.ReservationsResults;
import gr.opa.booking.common.requests.StatisticsResults;
import gr.opa.booking.common.utils.DateUtils;

public class MapReduceClient {
    private final TcpClient client;
    private final ClientApp clientApp;

    public MapReduceClient(ClientApp clientApp, String ip, int port) {
        this.clientApp = clientApp;

        client = new TcpClient(ip, port);
        new Thread(() -> client.connect()).start();
    }

    public void identifyClient() {
        new Thread(() -> {
            UUID clientId = UUID.randomUUID();
            client.setUuid(clientId);
            ClientIdentification cir = new ClientIdentification();
            cir.setData(clientId);
            try {
                client.sendRequest(cir);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void sendAddRoomsRequest() {
        new Thread(() -> {
            List<Room> rooms = null;
            ObjectMapper mapper = new ObjectMapper();
            try {
                rooms = mapper.readValue(clientApp.getAssets().open("rooms.json"),
                        new TypeReference<List<Room>>() {
                        });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            if (rooms == null || rooms.isEmpty()) {
                System.err.println("Could not read rooms from JSON");
                return;
            }

            Random random = new Random();
            Date today = DateUtils.today();

            Date from = DateUtils.addDays(today, 0);
            Date to = DateUtils.addDays(today, 60);

            for (Room room : rooms) {
                Range<Date> availability = new Range<>();
                availability.setFrom(DateUtils.addDays(from, -5 + random.nextInt(10)));
                availability.setTo(DateUtils.addDays(to, -5 + random.nextInt(10)));
                room.setAvailableDates(availability);

                AddRoom roomRequest = new AddRoom();
                roomRequest.setData(room);
                try {
                    client.sendRequest(roomRequest);
                } catch (JsonProcessingException e) {
                    System.err.println(e);
                }
            }
        }).start();
    }

    void executeSearch(SearchFilters searchFilters, ArrayList<Room> results, MyRoomListAdapter adapter) {
        new Thread(() -> {
            Log.v("Search filter: ", String.valueOf(searchFilters));
            MasterSearch sr = new MasterSearch();
            sr.setClientId(client.getUuid());
            sr.setData(searchFilters);
            try {
                client.sendRequest(sr);

                String response;
                while((response = client.waitForMessage()) != null) {
                    Log.v("Search Message Type: ", response);
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode responseJson = mapper.readTree(response);
                    MessageType messageType = MessageType.valueOf(
                            responseJson.findPath("messageType").textValue());

                    if (messageType.equals(MessageType.CLIENT_RESULTS)) {

                        ClientSearchResults csrr = mapper.readValue(response, ClientSearchResults.class);

                        new Handler(Looper.getMainLooper()).post(() -> {
                            results.clear();
                            results.addAll(csrr.getData());
                            adapter.notifyDataSetChanged();

                            Log.v("Room List (adapter) - ", results.toString());
                        });

                        Log.v("Search Reply: ", csrr.getData().toString());
                        break;
                    }
                }
            }catch (IOException e) {
                System.err.println(e);
            }
        }).start();
    }

    void sendBooking(Booking b, SuccessOrFailureHandler handler) {
        new Thread(() -> {
            BookRoom bookRoomRequest = new BookRoom();
            bookRoomRequest.setData(b);
            try {
                client.sendRequest(bookRoomRequest);

                String response;
                while((response = client.waitForMessage()) != null) {

                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode responseJson = mapper.readTree(response);
                    MessageType messageType = MessageType.valueOf(
                            responseJson.findPath("messageType").textValue());

                    Log.v("Booking Message Type: ", response);

                    if (messageType.equals(MessageType.BOOKING_FAILURE) ||
                            messageType.equals(MessageType.BOOKING_SUCCESS)) {

                        new Handler(Looper.getMainLooper()).post(() -> {
                            if (messageType.equals(MessageType.BOOKING_SUCCESS)) {
                                handler.onSuccess();
                                return;
                            }

                            handler.onFailure();
                        });
                        break;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }



    }).start();
    }

    void sendRate(RoomRating ratingModel) {
        new Thread(() -> {
            RateRoom rr = new RateRoom();
            rr.setData(ratingModel);
            try {
                client.sendRequest(rr);
                Log.v("RateRoom: ", rr.getData().toString());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    void sendStatistics(BookingStatisticsCriteria criteria, List<String> areaStat, List<Integer> counterStat, MyStatisticListAdapter adapter) {
        new Thread(() -> {
            BookingStatistics bs = new BookingStatistics();
            bs.setClientId(client.getUuid());
            bs.setData(criteria);
            try {
                client.sendRequest(bs);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            try
            {
                String response;
                while((response = client.waitForMessage()) != null) {

                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode responseJson = mapper.readTree(response);
                    MessageType messageType = MessageType.valueOf(
                            responseJson.findPath("messageType").textValue());

                    if (messageType == MessageType.STATISTICS_RESULTS) {
                        StatisticsResults rs = mapper.treeToValue(responseJson, StatisticsResults.class);
                        RoomStatistics roomStats = rs.getData();

                        new Handler(Looper.getMainLooper()).post(() -> {
                            areaStat.clear();
                            counterStat.clear();
                            for (String key : roomStats.getBookingsCountPerArea().keySet()) {
                                areaStat.add(key);
                                counterStat.add(roomStats.getBookingsCountPerArea().get(key));
                                adapter.notifyDataSetChanged();
                                Log.v("Area List (adapter) - ", areaStat.toString());
                                Log.v("Counter List (adapter) - ", counterStat.toString());
                            }
                        });


                        for (String key : roomStats.getBookingsCountPerArea().keySet()) {
                            Log.v("Statistic Results: ", key + " " +String.valueOf(roomStats.getBookingsCountPerArea().get(key)));
                        }
                        break;
                    }
                }
            }catch (Exception e) {
                throw new RuntimeException(e);
            }

        }).start();
    }

    void sendReservations(RoomReservationsCriteria criteria, List<Booking> bookings, MyBookingListAdapter adapter) {
        new Thread(() -> {
            ManagerReservations mr = new ManagerReservations();
            mr.setClientId(client.getUuid());
            mr.setData(criteria);
            try {
                client.sendRequest(mr);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            try
            {
                String response;
                while((response = client.waitForMessage()) != null) {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode responseJson = mapper.readTree(response);
                    MessageType messageType = MessageType.valueOf(
                            responseJson.findPath("messageType").textValue());

                    if (messageType == MessageType.RESERVATIONS_RESULTS) {
                        ReservationsResults rr = mapper.treeToValue(responseJson, ReservationsResults.class);
                        RoomReservations roomReservations = rr.getData();
                        Log.v("Room Reservations data: ", roomReservations.getReservations().toString());

                        new Handler(Looper.getMainLooper()).post(() -> {
                            bookings.clear();
                            for (String roomName : roomReservations.getReservations().keySet()) {
                                bookings.addAll(roomReservations.getReservations().get(roomName));
                                adapter.notifyDataSetChanged();
                                Log.v("Bookings List (adapter) - ", bookings.toString());
                            }
                        });
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }).start();
    }
}

