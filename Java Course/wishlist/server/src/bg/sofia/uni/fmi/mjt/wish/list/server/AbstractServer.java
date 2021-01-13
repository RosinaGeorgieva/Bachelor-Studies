package bg.sofia.uni.fmi.mjt.wish.list.server;

import bg.sofia.uni.fmi.mjt.wish.list.server.exception.ClientDisconnectedException;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractServer implements Server{
    static final String SERVER_HOST = "localhost";
    static final int BUFFER_SIZE = 1024;
    static final String LINE_SEPARATOR = System.lineSeparator();
    static final String END_OF_STREAM_EXCEPTION_MESSAGE = "[ The channel has reached end-of-stream ]" + LINE_SEPARATOR;
    static final String CONNECTION_PROBLEM_MESSAGE = "[ There is a problem with the server connection ]" + LINE_SEPARATOR;
    static final String DISCONNECTED_FROM_SERVER = "[ Disconnected from server ]" + LINE_SEPARATOR;

    protected final Set<SocketChannel> clientSocketChannels;
    protected Integer sessionId = 0;
    protected int serverPort;
    protected boolean isRunning;
    protected ServerSocketChannel serverSocketChannel;
    protected Selector selector;
    protected ByteBuffer buffer;

    public AbstractServer(int serverPort) {
        this.serverPort = serverPort;
        this.isRunning = false;
        this.clientSocketChannels = new HashSet<>();
    }

    @Override
    public void start() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, serverPort));
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            buffer = ByteBuffer.allocate(BUFFER_SIZE);
            isRunning = true;
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new RuntimeException(CONNECTION_PROBLEM_MESSAGE);
        }
    }

    @Override
    public void stop() {
        try {
            isRunning = false;
            for (SocketChannel socketChannel : clientSocketChannels) {
                buffer.put(DISCONNECTED_FROM_SERVER.getBytes(StandardCharsets.UTF_8));
                socketChannel.write(buffer);
            }
            selector.close();
            serverSocketChannel.close();
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new RuntimeException(CONNECTION_PROBLEM_MESSAGE);
        }
    }

    @Override
    public void receiveConnection(SelectionKey key) {
        try {
            ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
            SocketChannel accept = sockChannel.accept();
            clientSocketChannels.add(accept);
            accept.configureBlocking(false);
            accept.register(selector, SelectionKey.OP_READ).attach(sessionId);
            sessionId++;
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new RuntimeException(CONNECTION_PROBLEM_MESSAGE);
        }
    }

    @Override
    public String receiveRequest(SocketChannel socketChannel) throws IOException, ClientDisconnectedException {
        buffer.clear();
        int r = socketChannel.read(buffer);
        if (r < 0) {
            socketChannel.close();
            throw new ClientDisconnectedException("nehsto si");
        }
        buffer.flip();
        return new String(buffer.array(), 0, buffer.limit());
    }

    @Override
    public String sendResponse(SocketChannel socketChannel, String request) {
        String response = processRequest(request);
        try {
            buffer.clear();
            buffer.put(response.getBytes(StandardCharsets.UTF_8));
            buffer.flip();
            socketChannel.write(buffer);
            return response;
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new RuntimeException(CONNECTION_PROBLEM_MESSAGE);
        }
    }
}
