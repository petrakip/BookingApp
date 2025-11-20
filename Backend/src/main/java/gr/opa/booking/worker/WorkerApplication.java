package gr.opa.booking.worker;

import gr.opa.booking.common.Configuration;
import gr.opa.booking.common.ConfigurationReader;

import java.io.IOException;
import java.util.TimeZone;

public class WorkerApplication
{
    public static void main(String[] args) throws IOException {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        System.out.println("Client starting...");

        ConfigurationReader cfgReader = new ConfigurationReader();
        Configuration configuration = cfgReader.getConfiguration();
        System.out.println("Configuration: " + configuration);

        Worker worker = new Worker(configuration.getMasterHost(),
                configuration.getMasterPort(),
                configuration.getReducerHost(),
                configuration.getReducerPort());
        worker.start();
    }
}
