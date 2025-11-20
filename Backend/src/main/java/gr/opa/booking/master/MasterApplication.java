package gr.opa.booking.master;

import gr.opa.booking.common.Configuration;
import gr.opa.booking.common.ConfigurationReader;

import java.io.IOException;
import java.util.TimeZone;

public class MasterApplication
{
    public static void main(String[] args) throws IOException {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        System.out.println("Master starting...");

        ConfigurationReader cfgReader = new ConfigurationReader();
        Configuration configuration = cfgReader.getConfiguration();
        System.out.println("Configuration:" + configuration);

        System.out.printf("Started listening on port %d%n", configuration.getMasterPort());
        Master master = new Master(configuration.getMasterPort(), configuration.getNumberOfReplicas());
        master.start();
    }
}
