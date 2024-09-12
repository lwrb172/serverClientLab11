package com.lab11;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private List<ClientHandler> handlers = new ArrayList<>();

    public Server() throws IOException {
        serverSocket = new ServerSocket(3000);
    }

    public void listen() throws IOException {
        System.out.println("Server started!");
        while (true) {
            Socket socket = serverSocket.accept();
            ClientHandler handler = new ClientHandler(socket, this);
            Thread thread = new Thread(handler);
            thread.start();
            handlers.add(handler);
        }
    }

    public void broadcast(String message) {
        handlers.forEach(clientHandler -> clientHandler.send(message));
    }

    public void removeHandler(ClientHandler handler) {
        handlers.remove(handler);
    }

    public void disconnectHandlers() {
        handlers.forEach(handler -> handler.send("Bye!"));
        handlers.clear();
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();

        Runtime.getRuntime().addShutdownHook(new Thread(server::disconnectHandlers));

        server.listen();
    }
}
