package gr.opa.booking.worker;

import gr.opa.booking.common.TcpClient;
import gr.opa.booking.common.models.*;
import gr.opa.booking.common.requests.RegisterWorker;
import gr.opa.booking.common.requests.WorkerAlive;
import gr.opa.booking.common.utils.DateUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Worker {
    private final String masterHost;
    private final int masterPort;

    private final String reducerHost;
    private final int reducerPort;

    private ArrayList<Room> rooms = new ArrayList<>();
    private final Object roomsLock = new Object();

    private final HashMap<Room, List<Booking>> bookings = new HashMap<>();
    private final Object bookingsLock = new Object();

    private final HashMap<Room, List<Date>> availableDates = new HashMap<>();
    private final Object availableDatesLock = new Object();


    public Worker(String masterHost, int masterPort, String reducerHost, int reducerPort) {
        this.masterHost = masterHost;
        this.masterPort = masterPort;
        this.reducerHost = reducerHost;
        this.reducerPort = reducerPort;
    }

    public void start() throws IOException {
        TcpClient reducer = new TcpClient(reducerHost, reducerPort);
        reducer.connect();
        System.out.printf("Worker connected to reducer [%s:%d]%n", reducerHost, reducerPort);

        TcpClient master = new TcpClient(masterHost, masterPort);
        master.connect();
        System.out.printf("Worker connected to master [%s:%d]%n", masterHost, masterPort);

        RegisterWorker registerWorkerRequest = new RegisterWorker();
        master.sendRequest(registerWorkerRequest);

        new Thread(() -> {
            while(true) {
                try {
                    master.sendRequest(new WorkerAlive());
                    Thread.sleep(100);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    break;
                }
            }
        }).start();

        String requestBody;
        while((requestBody = master.waitForMessage()) != null) {
            new RequestHandler(this, requestBody, master, reducer).start();
        }
    }

    public void saveRoom(Room room) {
        synchronized (roomsLock) {
            ArrayList<Room> roomsWithThatName = rooms;
            if (roomsWithThatName.isEmpty()) {
                roomsWithThatName.add(room);

                List<Date> dates = DateUtils.getDatesForRange(room.getAvailableDates());
                availableDates.put(room, dates);
            } else {
                if (!isRoomInList(room, roomsWithThatName)) {
                        roomsWithThatName.add(room);

                        List<Date> dates = DateUtils.getDatesForRange(room.getAvailableDates());
                        availableDates.put(room, dates);
                    }
                }
            rooms = roomsWithThatName;
            }
    }


    public boolean isRoomInList(Room room, ArrayList<Room> roomsList) {
        for (Room r : roomsList) {
            if (r.getRoomName().equals(room.getRoomName())) {
                return true;
            }
        }
        return false;
    }

    public List<Room> roomsSearch(SearchFilters searchFilters) {
        List<Room> results = new ArrayList<>();

        synchronized (roomsLock) {
            for(Room room : rooms) {
                if (searchFilters.Match(room, this)) results.add(room);
            }
        }

        return results;
    }

    public boolean saveBooking(Booking booking) {
        Room room;

        synchronized (roomsLock) {
            List<Room> roomsWithThatName = rooms
                    .stream()
                    .filter(x -> x.getRoomName().equals(booking.getRoomName()))
                    .toList();

            if (roomsWithThatName.size() != 1) {
                return false;
            }

            room = roomsWithThatName.get(0);
        }

        List<Date> bookingDates = DateUtils
                .getDatesForRange(booking.getFrom(), booking.getTo());

        synchronized (bookingsLock) {
            if (!bookings.containsKey(room)) {
                bookings.put(room, new ArrayList<>());
                bookings.get(room).add(booking);
                removeDatesFromAvailability(room, bookingDates);
                return true;
            }

            if (isAvailableForDates(room, bookingDates)) {
                bookings.get(room).add(booking);
                removeDatesFromAvailability(room, bookingDates);
                return true;
            }

            return false;
        }
    }

    private void removeDatesFromAvailability(Room room, List<Date> bookingDates) {
        List<Date> roomAvailableDates;

        synchronized (availableDatesLock) {
            roomAvailableDates = availableDates.get(room);
            roomAvailableDates = roomAvailableDates
                    .stream().filter(x -> !bookingDates.contains(x))
                    .toList();
            availableDates.put(room, roomAvailableDates);
        }
    }

    public boolean isAvailableForDates(Room room, List<Date> dateCriteria) {
        List<Date> roomAvailableDates;

        synchronized (availableDatesLock) {
            roomAvailableDates = availableDates.get(room);
        }

        for(Date date : dateCriteria) {
            if (roomAvailableDates.stream().noneMatch(x -> x.compareTo(date) == 0)) {
                System.out.printf("Room %s is not available: Date %s not available%n",
                        room.getRoomName(), date.toString());
                return false;
            }
        }

        System.out.printf("Room %s is available%n", room.getRoomName());
        return true;
    }

    public void addRoomRating(String roomName, float rating) {
        synchronized (roomsLock) {
            for(Room room: rooms) {
                if (room.getRoomName().equals(roomName)) {
                    float ratingSoFar = room.getRating();
                    int numberOfRatingsSoFar = room.getNumberOfReviews();
                    float newRating = ((ratingSoFar * numberOfRatingsSoFar) + rating) / (numberOfRatingsSoFar + 1);
                    room.setRating(newRating);
                    room.setNumberOfReviews(numberOfRatingsSoFar + 1);
                }
            }
        }
    }

    public RoomStatistics roomStatistics(BookingStatisticsCriteria criteria) {
        RoomStatistics result = new RoomStatistics();
        HashMap<String, Integer> bookingsPerArea = new HashMap<>();

        synchronized (roomsLock) {
            synchronized (bookingsLock) {
                for (Room room : rooms) {
                    if (!bookings.containsKey(room)) continue;

                    if (!bookingsPerArea.containsKey(room.getArea())) {
                        bookingsPerArea.put(room.getArea(), 0);
                    }

                    int count = 0;
                    for (Booking booking : bookings.get(room)) {

                        if (booking.getFrom().compareTo(criteria.getBookingPeriodFrom()) >= 0
                            && booking.getFrom().compareTo(criteria.getBookingPeriodTo()) <= 0
                            && booking.getTo().compareTo(criteria.getBookingPeriodFrom()) >= 0
                            && booking.getTo().compareTo(criteria.getBookingPeriodTo()) <= 0)
                        {
                            count++;
                        }
                    }

                    bookingsPerArea.put(room.getArea(), bookingsPerArea.get(room.getArea()) + count);
                }
            }
        }

        result.setBookingsCountPerArea(bookingsPerArea);
        return result;
    }

    public RoomReservations roomReservations(RoomReservationsCriteria criteria) {
        RoomReservations roomReservations = new RoomReservations();
        HashMap<String, List<Booking>> bookingRooms = new HashMap<>();
        roomReservations.setReservations(bookingRooms);

        synchronized (roomsLock) {
            synchronized (bookingsLock) {
                for (Room room : bookings.keySet()) {
                    if (room.getManagerName().equals(criteria.getManagerName())) {
                        bookingRooms.put(room.getRoomName(), bookings.get(room));
                    }
                }
            }
        }

        return roomReservations;
    }
}
