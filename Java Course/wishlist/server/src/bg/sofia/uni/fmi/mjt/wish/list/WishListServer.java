package bg.sofia.uni.fmi.mjt.wish.list;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

//N.B. METODITE START I STOP i konstruktor
//N.B. VSQKO SYOBSHTENIE DA E S LINE SEPARATOR
public class WishListServer {

    private static final String SERVER_HOST = "localhost";
    private static final int AUTH_SERVER_PORT = 7778;
    private static final int BUFFER_SIZE = 1024;
    private static final String CHARSET = "UTF-8";

    private static final String REGISTER = "register";
    private static final String LOGIN = "login";
    private static final String LOGOUT = "logout";
    private static final String POST_WISH = "post-wish";
    private static final String GET_WISH = "get-wish";
    private static final String DISCONNECT = "disconnect";

    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String SERVER_CONNECTION_PROBLEM_MESSAGE = "[ There is a problem with the server connection ]" + LINE_SEPARATOR;
    private static final String UKNOWN_COMMAND_MESSAGE = "[ Unknown command ]" + LINE_SEPARATOR;
    private static final String NOT_ENOUGH_ARGUMENTS_EXCEPTION_MESSAGE = "[ Too few arguments provided. ]" + LINE_SEPARATOR;
    private static final String AUTH_REQUEST_FORMAT = "%d %s";
    private static final String DISCONNECTED_FROM_SERVER = "[ Disconnected from server ]" + LINE_SEPARATOR;
    private static final String OK = "-*&OK&*-" + LINE_SEPARATOR;

    private static final WishListRepository wishList = new WishListRepository();
    private static int serverPort = 7777;
    private static Integer sessionId = 0;

    private static ServerSocketChannel serverSocketChannel; //DA MAHNA STATIC!!!
    private static Selector selector;//hmm static?
    private static SocketChannel socketChannel;
    private static Reader reader;
    private static Writer writer;
    private static boolean shouldRun = true;

    private static Set<SocketChannel> clients = new HashSet<>();

    public WishListServer(int serverPort) {
        this.serverPort = serverPort;
    }

    public static void main(String[] args) {
        start();
    }

    public static void start() { //da mahna static
        try {
            shouldRun = true;

            serverSocketChannel = ServerSocketChannel.open();
            socketChannel = SocketChannel.open();
            reader = new BufferedReader(Channels.newReader(socketChannel, CHARSET));
            writer = new PrintWriter(Channels.newWriter(socketChannel, CHARSET), true);

            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, serverPort));
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            socketChannel.connect(new InetSocketAddress(SERVER_HOST, AUTH_SERVER_PORT));

            run();

        } catch (IOException exception) {
            System.out.println(SERVER_CONNECTION_PROBLEM_MESSAGE);
            exception.printStackTrace();
        }
    }

    public static void stop() {
        try {
            shouldRun = false;
            reader.close();
            writer.close();
            socketChannel.close();
            for (SocketChannel client : clients) {
                client.close();
            }
            selector.close();
            serverSocketChannel.close();
        } catch (IOException exception) {
            System.out.println(SERVER_CONNECTION_PROBLEM_MESSAGE);
            exception.printStackTrace();
        }
    }

    public static void run() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            while (shouldRun) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();

                        buffer.clear();
                        int r = sc.read(buffer);
                        if (r < 0) {
                            sc.close();
                            continue;
                        }

                        buffer.flip();

                        String request = new String(buffer.array(), 0, buffer.limit());
                        String response = processRequest((Integer) key.attachment(), request, reader, writer);

                        buffer.clear();
                        buffer.put(response.getBytes(StandardCharsets.UTF_8));
                        buffer.flip();
                        sc.write(buffer);

                    } else if (key.isAcceptable()) {
                        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
                        SocketChannel accept = sockChannel.accept();
                        clients.add(accept);
                        accept.configureBlocking(false);
                        accept.register(selector, SelectionKey.OP_READ).attach(sessionId);
                        sessionId++;
                    }
                    keyIterator.remove();
                }
            }
        } catch (IOException exception) {
            System.out.println(SERVER_CONNECTION_PROBLEM_MESSAGE);
            exception.printStackTrace();
        }
    }

    private static String processRequest(Integer sessionId, String request, Reader reader, Writer writer) {
        try {
            String command = Extractor.extractCommand(request);
            String authenticationResponse = requestAuthentication(addSessionIdToRequest(sessionId, request), reader, writer);
            switch (command) {
                case REGISTER:
                case LOGIN:
                    SessionsRepository.add(sessionId, Extractor.extractUser(request));
                    return authenticationResponse;
                case LOGOUT:
                    SessionsRepository.remove(sessionId);
                    return authenticationResponse;
                case POST_WISH:
                    if (!authenticationResponse.equals(OK)) {
                        return authenticationResponse;
                    }
                    return wishList.postWish(Extractor.extractUser(request), Extractor.extractWish(request));
                case GET_WISH:
                    if (!authenticationResponse.equals(OK)) {
                        return authenticationResponse;
                    }
                    return wishList.getRandomWishList(sessionId);
                case DISCONNECT:
                    return DISCONNECTED_FROM_SERVER;
                default:
                    return UKNOWN_COMMAND_MESSAGE;
            }
        } catch (NotEnoughArgumentsException exception) {
            return NOT_ENOUGH_ARGUMENTS_EXCEPTION_MESSAGE;
        }
    }

    private static String requestAuthentication(String request, Reader reader, Writer writer) {
        try {
            ((PrintWriter) writer).println(request);
            String reply = ((BufferedReader) reader).readLine();
            writer.flush();
            return reply + System.lineSeparator();
        } catch (IOException exception) {
            exception.printStackTrace();
            return SERVER_CONNECTION_PROBLEM_MESSAGE;
        }
    }

    private static String addSessionIdToRequest(Integer clientId, String request) {
        return String.format(AUTH_REQUEST_FORMAT, clientId, request);
    }
}