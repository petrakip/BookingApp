package gr.opa.booking.common.requests;

import gr.opa.booking.common.enums.MessageType;
import gr.opa.booking.common.models.RoomReservationsCriteria;

import java.util.UUID;

public class WorkerReservations extends Message<RoomReservationsCriteria> {
    public int workerNumber;
    private int totalWorkers;
    private UUID reservationsId;
    private UUID clientId;

    public WorkerReservations() {
        setMessageType(MessageType.WORKER_RESERVATIONS);
    }

    public static WorkerReservations from(ManagerReservations managerReservations) {
        WorkerReservations result = new WorkerReservations();
        result.setData(managerReservations.getData());
        result.setClientId(managerReservations.getClientId());
        return result;
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

    public void setWorkerNumber(int workerNumber) {
        this.workerNumber = workerNumber;
    }

    public int getWorkerNumber() {
        return workerNumber;
    }


}
