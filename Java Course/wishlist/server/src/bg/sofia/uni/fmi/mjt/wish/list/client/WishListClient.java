package bg.sofia.uni.fmi.mjt.wish.list.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

//N.B. List da mi e s glavno L navsqkyde
public class WishListClient implements Client {
    private static final String CHARSET = "UTF-8";
    private static final String SERVER_HOST = "localhost";
    private static final String REQUEST_INPUT = "=> ";
    private static final String CONNECTION_PROBLEM_MESSAGE = "[ There is a problem with the server connection ]";
    private static final String DISCONNECTED_FROM_SERVER = "[ Disconnected from server ]"; //DA VRYSHTAM TOVA KOGATO SI DISCONNECT-VA CLIENTITE

    private int serverPort = 7777;
    private boolean isConnectedToServer = false;
    private SocketChannel socketChannel;
    private BufferedReader reader;
    private PrintWriter writer;
    private Scanner scanner;

    public WishListClient(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public void connect() {
        try {
            socketChannel = SocketChannel.open();
            reader = new BufferedReader(Channels.newReader(socketChannel, CHARSET));
            writer = new PrintWriter(Channels.newWriter(socketChannel, CHARSET), true);
            scanner = new Scanner(System.in);

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, serverPort));
            isConnectedToServer = true;
        } catch (IOException exception) {
            throw new RuntimeException(CONNECTION_PROBLEM_MESSAGE);
        }
    }

    @Override
    public String sendRequest(SocketChannel socketChannel) {
        System.out.print(REQUEST_INPUT);
        String request = scanner.nextLine();
        writer.println(request);
        writer.flush();
        return request;
    }

    @Override
    public String receiveResponse(SocketChannel socketChannel) {
        try {
            String response = reader.readLine();
            return response;
        } catch (IOException exception) {
            throw new RuntimeException(CONNECTION_PROBLEM_MESSAGE);
        }
    }

    @Override
    public void disconnect() {
        isConnectedToServer = false;
        try {
            socketChannel.close();
            reader.close();
            writer.close();
            scanner.close();
        } catch (IOException exception) {
            throw new RuntimeException(CONNECTION_PROBLEM_MESSAGE);
        }
    }

    @Override
    public void run() {
        connect();
        while(isConnectedToServer) {
            sendRequest(this.socketChannel);
            String response = receiveResponse(this.socketChannel);

            if(response.equals(DISCONNECTED_FROM_SERVER)) {
                disconnect();
                break;
            }
        }
    }
}
