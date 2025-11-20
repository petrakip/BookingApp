package gr.opa.booking.reducer;

import gr.opa.booking.common.models.Room;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReduceResults {
    private final List<Room> results = new ArrayList<>();
    private final Set<Integer> workersDone = new HashSet<>();

    private final Object resultsLock = new Object();
    private final int totalWorkers;

    public ReduceResults(int totalWorkers) {
        this.totalWorkers = totalWorkers;
    }

    public void addResults(List<Room> rooms, int workerNumber) {
        synchronized (resultsLock) {
            if (workersDone.isEmpty() || (!workersDone.contains(workerNumber))) {
                workersDone.add(workerNumber);
                results.addAll(rooms);
            }
        }
    }

    public boolean resultsCollected() {
        synchronized (resultsLock) {
            return totalWorkers == workersDone.size();
        }
    }

    public List<Room> getResults() {
        synchronized (resultsLock) {
            return results;
        }
    }

    public void clearWorkersDone() {
        workersDone.clear();
    }

    public void clearResults() {
        results.clear();
    }
}
