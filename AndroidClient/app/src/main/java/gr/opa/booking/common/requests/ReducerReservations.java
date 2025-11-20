package gr.opa.booking.common.requests;

import java.util.UUID;

import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.models.RoomReservations;

public class ReducerReservations extends Message<RoomReservations> {
    private int totalWorkers;
    private UUID reservationsId;
    private UUID clientId;
    private int workerNumber;

    public ReducerReservations() {
        setMessageType(MessageType.REDUCER_RESERVATIONS);
    }

    public static ReducerReservations from(WorkerReservations workerReservations) {
        ReducerReservations reducerReservations = new ReducerReservations();
        reducerReservations.setReservationsId(workerReservations.getReservationsId());
        reducerReservations.setTotalWorkers(workerReservations.getTotalWorkers());
        reducerReservations.setClientId(workerReservations.getClientId());
        reducerReservations.setWorkerNumber(workerReservations.getWorkerNumber());
        return reducerReservations;
    }

    private void setWorkerNumber(int workerNumber) {
        this.workerNumber = workerNumber;
    }

    public int getTotalWorkers() {
        return totalWorkers;
    }

    public void setTotalWorkers(int totalWorkers) {
        this.totalWorkers = totalWorkers;
    }

    public UUID getReservationsId() {
        return reservationsId;
    }

    public void setReservationsId(UUID reservationsId) {
        this.reservationsId = reservationsId;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public int getWorkerNumber() {
        return workerNumber;
    }
}

