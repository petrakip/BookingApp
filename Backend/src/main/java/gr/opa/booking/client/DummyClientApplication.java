package gr.opa.booking.client;

import gr.opa.booking.client.menus.MainMenu;
import gr.opa.booking.common.Configuration;
import gr.opa.booking.common.ConfigurationReader;
import gr.opa.booking.common.TcpClient;
import gr.opa.booking.common.requests.ClientIdentification;

import java.io.IOException;
import java.util.TimeZone;
import java.util.UUID;

public class DummyClientApplication
{
    public static void main(String[] args) throws IOException, InterruptedException {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC")); // synchronized time zone in program

        ConfigurationReader cfgReader = new ConfigurationReader();
        Configuration configuration = cfgReader.getConfiguration();
        System.out.println("Configuration: " + configuration);

        TcpClient tcpClient = new TcpClient(configuration.getMasterHost(), configuration.getMasterPort());
        tcpClient.connect();
        System.out.printf("Client connected to %s:%d%n", configuration.getMasterHost(), configuration.getMasterPort());

        UUID clientId = UUID.randomUUID();
        tcpClient.setUuid(clientId);
        ClientIdentification cir = new ClientIdentification();
        cir.setData(clientId);
        tcpClient.sendRequest(cir);

        MainMenu home = new MainMenu(tcpClient, clientId);
        home.start();
    }
}
