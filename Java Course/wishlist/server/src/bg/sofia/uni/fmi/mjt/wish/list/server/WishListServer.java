package bg.sofia.uni.fmi.mjt.wish.list.server;

import bg.sofia.uni.fmi.mjt.wish.list.auth.server.WishListAuthenticationServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class WishListServer extends AbstractServer {
    private static final String CHARSET = "UTF-8";
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 1024;
    private static final int AUTH_SERVER_PORT = 7778; //hmm?????
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String NO_REQUEST_RECEIVED_MESSAGE = "[ No request was received yet ]" + LINE_SEPARATOR;
    private static final String DISCONNECTED_FROM_SERVER = "[ Disconnected from server ]" + LINE_SEPARATOR;
    private static final String UKNOWN_COMMAND_MESSAGE = "[ Unknown command ]" + LINE_SEPARATOR;
    private static final String SUCCESSFUL_LOGOUT_MSG = "[ Successfully logged out ]" + LINE_SEPARATOR;
    private static final String NOT_ENOUGH_ARGUMENTS_EXCEPTION_MESSAGE = "[ Too few arguments provided. ]" + LINE_SEPARATOR;
    private static final String AUTH_REQUEST_FORMAT = "%d %s";
    private static final String OK = "-*&OK&*-" + LINE_SEPARATOR;

    private static final String REGISTER = "register";
    private static final String LOGIN = "login";
    private static final String LOGOUT = "logout";
    private static final String POST_WISH = "post-wish";
    private static final String GET_WISH = "get-wish";
    private static final String DISCONNECT = "disconnect";

    private final WishListRepository wishList;
    private Reader reader;
    private Writer writer;
    private SocketChannel socketChannel;

    private Server authenticationServer;

    public WishListServer(int serverPort) {
        super(serverPort);
        this.wishList = new WishListRepository();
        this.authenticationServer = new WishListAuthenticationServer(this.serverPort + 1);
    }

    @Override
    public void start() {
        try {
            //da ne zabravq da spiram v stop drugiq server
            socketChannel = SocketChannel.open();
            reader = new BufferedReader(Channels.newReader(socketChannel, CHARSET));
            writer = new PrintWriter(Channels.newWriter(socketChannel, CHARSET), true);

            new ServerThread(authenticationServer).start();
            socketChannel.connect(new InetSocketAddress(SERVER_HOST, AUTH_SERVER_PORT));

            super.start();

            runServer();
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new RuntimeException(CONNECTION_PROBLEM_MESSAGE);
        }
    }

    private void runServer() {
        try {
            while (isRunning) {
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
                        String request;

                        try {
                            request = receiveRequest(sc);
                        } catch (IOException exception) { //da vidq kakwo stava kato se disconnect-nat vsichki
                            continue;
                        }

                        Integer currentSessionId = (Integer) key.attachment();
                        request = String.format(AUTH_REQUEST_FORMAT, currentSessionId, request);
                        sendResponse(sc, request);
                    } else if (key.isAcceptable()) {
                        receiveConnection(key);
                    }
                    keyIterator.remove();
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new RuntimeException(CONNECTION_PROBLEM_MESSAGE);
        }
    }

    @Override
    public String processRequest(String request) {
        try {
            Integer currentSessionId = Extractor.extractSessionId(request);
            String command = Extractor.extractCommand(request);
            String authenticationResponse = requestAuthentication(request);
            switch (command) {
                case REGISTER:
                case LOGIN:
                    SessionsRepository.add(currentSessionId, Extractor.extractUser(request));
                    return authenticationResponse;
                case LOGOUT:
                    SessionsRepository.remove(currentSessionId);
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
                    return wishList.getRandomWishList(currentSessionId);
                case DISCONNECT:
                    return DISCONNECTED_FROM_SERVER;
                default:
                    return UKNOWN_COMMAND_MESSAGE;
            }
        } catch (NotEnoughArgumentsException exception) {
            exception.printStackTrace();
            return NOT_ENOUGH_ARGUMENTS_EXCEPTION_MESSAGE;
        }
    }

    private String requestAuthentication(String request) {
        try {
            ((PrintWriter) writer).println(request);
            String reply = ((BufferedReader) reader).readLine();
            writer.flush();
            return reply + System.lineSeparator();
        } catch (IOException exception) {
            exception.printStackTrace();
            return CONNECTION_PROBLEM_MESSAGE;
        }
    }
}
