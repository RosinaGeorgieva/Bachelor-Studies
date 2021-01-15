package bg.sofia.uni.fmi.mjt.wish.list.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

//list da mi e s glavno L navsqkyde; da probvam v main kak rabotqt
public class WishListClient extends AbstractClient { //da iztestvam s tozi klient, vmesto s onzi ot drugiq main;
    private static final String REQUEST_INPUT = "=> ";

    public WishListClient(int serverPort) {
        super(serverPort);
    }

    public void execute() {
        try {
            connect();
            while (isConnectedToServer) {
                System.out.println(REQUEST_INPUT);
                String request = reader.readLine();
                sendRequest(request);
                String response = receiveResponse();

                if (response.equals(DISCONNECTED_FROM_SERVER)) {
                    disconnect();
                    break;
                }
            }
        } catch (IOException exception) {
            //todo
        }
    }
}
