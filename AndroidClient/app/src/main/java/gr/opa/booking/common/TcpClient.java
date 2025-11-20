package gr.opa.booking.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

import gr.opa.booking.common.requests.Message;

public class TcpClient {
    private final String host;
    private final int port;
    private Socket socket;
    private boolean connected;
    private PrintWriter out;
    private BufferedReader in;
    private UUID uuid;

    public TcpClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public TcpClient(Socket socket) throws IOException {
        if (!socket.isConnected())
            throw new IOException("Can't create TcpClient from closed socket");

        host = socket.getInetAddress().getHostAddress();
        port = socket.getPort();
        this.socket = socket;
        out = new PrintWriter(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        connected = true;
    }

    public void connect() {
        if (connected) return;
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            connected = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // With this method, client sends request to the master
    public <T extends Message<?>> void sendRequest(T requestMessage)
            throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(requestMessage);
        System.out.printf("Sending request with type [%s]%n", requestMessage.getMessageType());
        out.write(payload + "\n");
        out.flush();
    }

    // With this method, client waits to receive response for his requests
    public String waitForMessage() throws IOException {
        return in.readLine();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void close() {
        try {
            socket.close();
            in.close();
            out.close();
        } catch (Exception e) {
            System.err.println("Something gone wrong in exiting...");
        }

    }
}
