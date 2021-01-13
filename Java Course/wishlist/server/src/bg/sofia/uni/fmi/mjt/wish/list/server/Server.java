package bg.sofia.uni.fmi.mjt.wish.list.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public interface Server {
    void start();

    void stop();

    void receiveConnection(SelectionKey key);

    String receiveRequest(SocketChannel socketChannel) throws IOException; //da go izmislq s drug exception!!! koito ne e checked

    String sendResponse(SocketChannel socketChannel, String request);

    String processRequest(String request);
}
