package com.javaproject.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Serg on 27-Mar-18.
 */
public class Server {
    public void start(int port) {
        startSocket(port);
    }

    private void startSocket(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket client = serverSocket.accept();
                createThreadForClient(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void createThreadForClient(Socket client) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                     while (!Thread.interrupted()) {
                         gettingClientMessages(client);
                     }
                } catch (IOException | ClassNotFoundException | InterruptedException e) {
                    Thread.yield();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void gettingClientMessages(Socket client) throws IOException, ClassNotFoundException, InterruptedException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        getStringFromSocket(bufferedReader, client);
        Thread.sleep(500L);

    }

    private void getStringFromSocket(BufferedReader bufferedReader, Socket client) throws IOException {
        String line;
        while (bufferedReader.ready()) {
            line = bufferedReader.readLine();
            handleRequest(line, client);
        }
    }

    private void handleRequest(String line, Socket client) throws IOException {
        System.out.println("Getting from client: " + line);

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        writer.write("Server echo: " + line + "\n");
        writer.flush();
        System.out.println("written: " + line);
    }
}
