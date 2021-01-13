package bg.sofia.uni.fmi.mjt.wish.list.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public abstract class AbstractClient implements Client {
    protected static final String CHARSET = "UTF-8";
    protected static final String SERVER_HOST = "localhost";
    protected static final int DEFAULT_SERVER_PORT = 7778;
    protected static final String CONNECTION_PROBLEM_MESSAGE = "[ There is a problem with the server connection ]";
    protected static final String DISCONNECTED_FROM_SERVER = "[ Disconnected from server ]"; //DA VRYSHTAM TOVA KOGATO SI DISCONNECT-VA CLIENTITE

    protected int serverPort;
    protected boolean isConnectedToServer = false;
    protected SocketChannel socketChannel;
    protected BufferedReader reader;
    protected PrintWriter writer;
    protected Scanner scanner;

    public AbstractClient() {
        this.serverPort = DEFAULT_SERVER_PORT;
    }

    public AbstractClient(int serverPort) {
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
    public String receiveResponse() {
        try {
            String response = reader.readLine();
            return response + System.lineSeparator();
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
    public void sendRequest(String request) {
        writer.println(request);
        writer.flush();
    }
}
