package gr.opa.booking.reducer;

import gr.opa.booking.common.models.RoomStatistics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ReduceStatistics {
    private RoomStatistics roomStatistics;
    private final Object roomStatisticsLock = new Object();
    private final int totalWorkers;
    private final Set<Integer> workersDone = new HashSet<>();
    HashMap<String, Integer> bookingsPerArea = new HashMap<>();

    public ReduceStatistics(int totalWorkers) {
        this.totalWorkers = totalWorkers;
    }

    public void addStats(RoomStatistics statistics, int workerNumber) {
        synchronized (roomStatisticsLock) {
            if (workersDone.isEmpty() || (!workersDone.contains(workerNumber))) {
                if (roomStatistics == null) {
                    roomStatistics = new RoomStatistics();
                    roomStatistics.setBookingsCountPerArea(new HashMap<>());
                    roomStatistics.setCriteria(statistics.getCriteria());
                }

//                HashMap<String, Integer> bookingsPerArea =
//                        roomStatistics.getBookingsCountPerArea();

                if (statistics.getBookingsCountPerArea() == null) {
                    workersDone.add(workerNumber);
                    return;
                }

                for (String key : statistics.getBookingsCountPerArea().keySet()) {
                    if (!bookingsPerArea.containsKey(key)) {
                        bookingsPerArea.put(key, 0);
                    }

                    bookingsPerArea.put(key,
                            bookingsPerArea.get(key)
                                + statistics.getBookingsCountPerArea().get(key)
                    );

                    System.out.printf("Total bookings counted: [%s] - Bookings of this payload: [%s]%n",
                            bookingsPerArea.get(key), statistics.getBookingsCountPerArea().get(key));
                }

                workersDone.add(workerNumber);
            }
        }
    }


    public boolean statsCollected() {
        return totalWorkers == workersDone.size();
    }

    public RoomStatistics getResults() {
        synchronized (roomStatisticsLock) {
            roomStatistics.setBookingsCountPerArea(bookingsPerArea);
            return roomStatistics;
        }
    }

    public void clearStatistics() {
        bookingsPerArea.clear();
    }
}
