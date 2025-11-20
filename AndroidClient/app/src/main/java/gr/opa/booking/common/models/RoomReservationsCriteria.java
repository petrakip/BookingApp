package gr.opa.booking.common.models;

import java.io.Serializable;

public class RoomReservationsCriteria implements Serializable {
    private String managerName;

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
}
