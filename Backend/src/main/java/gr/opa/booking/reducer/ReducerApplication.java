package gr.opa.booking.reducer;

import gr.opa.booking.common.Configuration;
import gr.opa.booking.common.ConfigurationReader;

import java.io.IOException;
import java.util.TimeZone;

public class ReducerApplication
{
    public static void main(String[] args) throws IOException {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        System.out.println("Reducer starting...");

        ConfigurationReader cfgReader = new ConfigurationReader();
        Configuration configuration = cfgReader.getConfiguration();
        System.out.println("Configuration:" + configuration);

        System.out.printf("Started listening on port %d%n", configuration.getReducerPort());
        Reducer reducer = new Reducer(configuration.getReducerPort(),
                configuration.getMasterHost(), configuration.getMasterPort());
        reducer.start();
    }
}
