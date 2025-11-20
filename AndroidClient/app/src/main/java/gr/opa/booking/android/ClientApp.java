package gr.opa.booking.android;

import android.app.Application;


public class ClientApp extends Application {
    private MapReduceClient client;

    public void connectToServer(String ip, int port) {
        if (client == null) {
            client = new MapReduceClient(this, ip, port);
            client.identifyClient();

        }

    }


    public MapReduceClient getClient() {
        return client;
    }
}
