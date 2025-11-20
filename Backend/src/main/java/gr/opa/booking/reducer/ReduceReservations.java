package gr.opa.booking.reducer;

import gr.opa.booking.common.models.RoomReservations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ReduceReservations {
    private RoomReservations roomReservations;
    private final Object roomReservationsLock = new Object();
    private final int totalWorkers;
    private final Set<Integer> workersDone = new HashSet<>();

    public ReduceReservations(int totalWorkers) {
        this.totalWorkers = totalWorkers;
    }

    public void addReservations(RoomReservations reservations, int workerNumber) {
        synchronized (roomReservationsLock) {
            if (workersDone.contains(workerNumber)) return;

            if (roomReservations == null) {
                roomReservations = new RoomReservations();
                roomReservations.setReservations(new HashMap<>());
                roomReservations.setCriteria(reservations.getCriteria());
            }
        }

        if (reservations.getReservations() != null) {
            for (String roomName : reservations.getReservations().keySet()) {
                if (!roomReservations.getReservations().containsKey(roomName)) {
                    roomReservations.getReservations().put(roomName, reservations.getReservations().get(roomName));
                }
            }
        }

        System.out.println("Reservations collected from worker: " + workerNumber);
        workersDone.add(workerNumber);
    }

    public boolean reservationsCollected() {
        return totalWorkers == workersDone.size();
    }

    public RoomReservations getResults() {
        synchronized (roomReservationsLock) {
            return roomReservations;
        }
    }
}
