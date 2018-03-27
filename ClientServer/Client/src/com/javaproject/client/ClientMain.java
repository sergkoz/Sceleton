package com.javaproject.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Serg on 27-Mar-18.
 */
public class ClientMain {

    public static void main(String[] args) {
        System.out.println("Client started");
        try {
            Socket socket = new Socket("localhost", 1024);

            BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            startReadingThread(socket);
            while (true) {

                Scanner scanner = new Scanner(System.in);
                String text = scanner.nextLine();
                System.out.println("try to send line: " + text);
                outputStream.write(text + "\n");
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startReadingThread(Socket socket) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader br = null;
                try {
                    System.out.println("reading thread");
                    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while (true) {
                        while (br.ready()) {
                            String s = br.readLine();
                            System.out.println(s);
                        }
                        Thread.sleep(500);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    Thread.yield();
                }

            }
        }).start();
    }
}
