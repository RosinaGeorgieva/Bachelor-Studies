package bg.sofia.uni.fmi.mjt.wish.list.client;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public interface Client {
    void connect();

    void sendRequest(String request);

    String receiveResponse(); //eventualno ot string??

    void disconnect();
}
