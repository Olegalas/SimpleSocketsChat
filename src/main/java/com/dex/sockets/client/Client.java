package com.dex.sockets.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by dexter on 02.01.17.
 */
class Client {

    private static final String CONNECTED_SUCCESS = "Connected success";

    private  Client(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        System.out.println(CONNECTED_SUCCESS);
        new Thread(new ReadSocket(socket)).start();
        new Thread(new WriteSocket(socket)).start();
    }

    static Client getClient(String host, int port) throws IOException {
        return new Client(host, port);
    }

    private class ReadSocket implements Runnable {

        private static final String RECEIVED_MESSAGE = "Received message from server - %s";
        private static final String SERVER_LEFT_CHAT_ROOM = "Server left chat room";
        private final Socket socket;

        ReadSocket(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            try(
            InputStream stream = socket.getInputStream();
            Scanner scanner = new Scanner(stream)
            ){

                while(true){
                    System.out.println(String.format(RECEIVED_MESSAGE, scanner.nextLine()));
                }

            }catch (Exception ignore){/*NOPE*/}
            System.out.println(SERVER_LEFT_CHAT_ROOM);
        }
    }

    private class WriteSocket implements Runnable {

        private static final int BUFFER_SIZE = 1000;
        private static final String ENTER_MESSAGE = "Enter message  - ";
        private static final String SERVER_LEFT_CHAT_ROOM = "Server left chat room";
        private final Socket socket;

        WriteSocket(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            byte[] buffer = new byte[BUFFER_SIZE];

             try {

                 while (true){
                     System.out.println(ENTER_MESSAGE);
                     int count = System.in.read(buffer, 0, buffer.length);
                     socket.getOutputStream().write(buffer, 0, count);
                 }

             } catch (IOException ignore) {/*NOPE*/}

            System.out.println(SERVER_LEFT_CHAT_ROOM);
        }
    }
}
