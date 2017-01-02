package com.dex.sockets.client;

import java.io.IOException;

/**
 * Created by dexter on 02.01.17.
 */
class Run {

    public static void main(String[] args) throws IOException {

        Client.getClient("localhost", 8080);

    }

}
