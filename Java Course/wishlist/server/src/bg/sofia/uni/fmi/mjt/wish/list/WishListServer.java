package bg.sofia.uni.fmi.mjt.wish.list;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
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
    private static final String DISCONNECT = "disconnect"; //da napravq ot tuk da se disconnectva vmesto ok klienta

    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String SERVER_CONNECTION_PROBLEM_MESSAGE = "[ There is a problem with the server connection ]" + LINE_SEPARATOR;
    private static final String UKNOWN_COMMAND_MESSAGE = "[ Unknown command ]" + LINE_SEPARATOR;
    private static final String AUTH_REQUEST_FORMAT = "%d %s";
    private static final String OK = "-*&OK&*-" + LINE_SEPARATOR;

    private static final WishListRepository wishList = new WishListRepository();
    private static int serverPort = 7777;
    private static Integer clientId = 0; //HMM??

    public WishListServer(int serverPort) {
        this.serverPort = serverPort;
    }

    public static void main(String[] args) {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, serverPort));
            serverSocketChannel.configureBlocking(false);

            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            SocketChannel socketChannel = SocketChannel.open();
            BufferedReader reader = new BufferedReader(Channels.newReader(socketChannel, CHARSET));
            PrintWriter writer = new PrintWriter(Channels.newWriter(socketChannel, CHARSET), true);

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, AUTH_SERVER_PORT));

            //SERVER KOD
            while (true) {
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

                        String authRequest = addClientIdToRequest(currentClientId(key), request);
                        String authResponse = requestAuthentication(authRequest, reader, writer);

                        String response;
                        if (!authResponse.equals(OK)) {
                            response = authResponse;
                        } else {
                            response = process(request);
                        }

                        buffer.clear();
                        buffer.put(response.getBytes(StandardCharsets.UTF_8));

                        buffer.flip();
                        sc.write(buffer);

                    } else if (key.isAcceptable()) {
                        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
                        SocketChannel accept = sockChannel.accept();
                        accept.configureBlocking(false);
                        accept.register(selector, SelectionKey.OP_READ).attach(clientId);
                        clientId++;
                    }
                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            System.out.println(SERVER_CONNECTION_PROBLEM_MESSAGE);
        }
    }

    private static String process(String request) {
        try {
            String command = Extractor.extractCommand(request);
            if (command.equals("post-wish")) {
                String name = Extractor.extractName(request);
                Wish wish = Extractor.extractWish(request);
                return wishList.postWish(name, wish);
            } else if (command.equals("get-wish")) {
                return wishList.getRandomWishList();
            }
            return UKNOWN_COMMAND_MESSAGE;
        } catch (NotEnoughArgumentsException exception) {
            return exception.getMessage() + System.lineSeparator();
        }
    }

    private static String requestAuthentication(String request, Reader reader, Writer writer) {
        try {
            ((PrintWriter) writer).println(request);
            String reply = ((BufferedReader) reader).readLine();
            writer.flush();
            return reply + System.lineSeparator();
        } catch (IOException e) {
            return SERVER_CONNECTION_PROBLEM_MESSAGE;
        }
    }

    private static String addClientIdToRequest(Integer clientId, String request) {
        return String.format(AUTH_REQUEST_FORMAT, clientId, request);
    }

    private static Integer currentClientId(SelectionKey key) {
        return (Integer) key.attachment();
    }

//    private static String processRequest(String request) throws NotEnoughArgumentsException {
//        String command = Extractor.extractCommand(request);
//        switch(command) {
//            case REGISTER: return register(request);
//            case LOGIN: return login(request);
//            case LOGOUT: return logout(request);
//            case GET_WISH: return getWish(request);
//            case POST_WISH: return postWish(request);
////            case DISCONNECT: return Command.DISCONNECT; //TUK KAKWO?????
//            default: return UKNOWN_COMMAND_MESSAGE;
//        }
//    }
}