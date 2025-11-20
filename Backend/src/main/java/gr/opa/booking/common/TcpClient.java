package gr.opa.booking.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr.opa.booking.common.requests.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class TcpClient {
    private final String host;
    private final int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private UUID uuid;
    private LocalDateTime lastAliveAt;

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
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    public void connect() throws IOException {
        if (isConnected()) return;
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    // With this method, client sends request to the master
    public <T extends Message<?>> void sendRequest(T requestMessage) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(requestMessage);
        // Debug: System.out.printf("Sending request with type [%s]%n", requestMessage.getMessageType());
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

    public void alive(Socket clientSocket) {
        if (socket.equals(clientSocket)) {
            lastAliveAt = LocalDateTime.now();
        }
    }

    public boolean isDead() {
        return LocalDateTime.now().isAfter(lastAliveAt.plus(200, ChronoUnit.MILLIS));
    }
}
