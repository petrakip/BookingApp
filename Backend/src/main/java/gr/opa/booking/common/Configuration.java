package gr.opa.booking.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Configuration {
    @JsonProperty("master_port")
    private int masterPort;

    @JsonProperty("master_host")
    private String masterHost;

    @JsonProperty("reducer_port")
    private int reducerPort;

    @JsonProperty("reducer_host")
    private String reducerHost;

    @JsonProperty("numberOfReplicas")
    private int numberOfReplicas;

    public int getNumberOfReplicas() {
        return numberOfReplicas;
    }

    public void setNumberOfReplicas(int numberOfReplicas) {
        this.numberOfReplicas = numberOfReplicas;
    }

    public int getMasterPort() {
        return masterPort;
    }

    public void setMasterPort(int masterPort) {
        this.masterPort = masterPort;
    }

    public String getMasterHost() {
        return masterHost;
    }

    public void setMasterHost(String masterHost) {
        this.masterHost = masterHost;
    }

    public int getReducerPort() {
        return reducerPort;
    }

    public void setReducerPort(int reducerPort) {
        this.reducerPort = reducerPort;
    }

    public String getReducerHost() {
        return reducerHost;
    }

    public void setReducerHost(String reducerHost) {
        this.reducerHost = reducerHost;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "masterPort=" + masterPort +
                ", masterHost='" + masterHost + '\'' +
                ", reducerPort=" + reducerPort +
                ", reducerHost='" + reducerHost + '\'' +
                ", numberOfReplicas=" + numberOfReplicas +
                '}';
    }
}
