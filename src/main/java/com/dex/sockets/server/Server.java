package com.dex.sockets.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by dexter on 28.12.16.
 */
class Server {

    private static final String NEW_SOCKET_WAS_ACCEPTED = "New socket was accepted";
    private static final String SERVER_WAS_RUN = "Server was run";
    private ServerSocket serverSocket = new ServerSocket(8080);


    Server() throws IOException {
    }


    void start() throws IOException {
        System.out.println(SERVER_WAS_RUN);
        while(true){
            Socket socket = serverSocket.accept();
            System.out.println(NEW_SOCKET_WAS_ACCEPTED);
            new Thread(new ReadSocket(socket)).start();
            new Thread(new WriteSocket(socket)).start();
        }
    }

    private class ReadSocket implements Runnable {

        private static final String CLIENT_LEFT_CHAT_ROOM = "Client left chat room";
        private static final String TEMPLATE = "message from %s - %s";
        private Socket socket;

        private ReadSocket(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            try (InputStream stream = socket.getInputStream();
                 InputStreamReader inputStreamReader = new InputStreamReader(stream);
                 BufferedReader reader = new BufferedReader(inputStreamReader)
            ) {
                while(true){
                    String msg = reader.readLine();
                    if(msg == null) break;
                    System.out.println(String.format(TEMPLATE, socket.getInetAddress().getHostName(), msg));
                }
            } catch (IOException ignore){/*NOPE*/}
            System.out.println(CLIENT_LEFT_CHAT_ROOM);

        }
    }

    private class WriteSocket implements Runnable {

        private static final String CLIENT_LEFT_CHAT_ROOM = "Client left chat room";
        private static final String ENTER_MESSAGE_TO_CLIENT = "Enter message to client %s - ";
        private static final int BUFFER_SIZE = 1000;
        private Socket socket;

        private WriteSocket(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            try  {
                byte[] buffer = new byte[BUFFER_SIZE];
                while (true){
                    System.out.print(String.format(ENTER_MESSAGE_TO_CLIENT, socket.getInetAddress().getHostName()));
                    int count = System.in.read(buffer, 0, buffer.length);
                    socket.getOutputStream().write(buffer, 0, count);
                }
            } catch (IOException e){
                System.out.println(CLIENT_LEFT_CHAT_ROOM);
            }

        }
    }

}
