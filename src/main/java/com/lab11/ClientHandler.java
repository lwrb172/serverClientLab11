package com.lab11;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Server server;
    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.server = server;
        this.socket = socket;
        InputStream input = socket.getInputStream();
        OutputStream output = socket.getOutputStream();
        reader = new BufferedReader(new InputStreamReader(input));
        writer = new PrintWriter(output, true);
    }

    @Override
    public void run() {
        System.out.println("Client connected!");
        String message;
        try {
            while ((message = reader.readLine()) != null)
                server.broadcast(message);
            close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Client disconnected!");
    }

    public void send(String message) {
        writer.println(message);
    }

    private void close() throws IOException {
        socket.close();
        server.removeHandler(this);
    }
}
