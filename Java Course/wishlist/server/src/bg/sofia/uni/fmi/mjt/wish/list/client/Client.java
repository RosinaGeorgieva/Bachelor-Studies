package bg.sofia.uni.fmi.mjt.wish.list.client;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public interface Client {
    void connect();

    String sendRequest(SocketChannel socketChannel);

    String receiveResponse(SocketChannel socketChannel); //eventualno ot string??

    void disconnect();

    void run();
}
