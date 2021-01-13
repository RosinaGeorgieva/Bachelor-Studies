package bg.sofia.uni.fmi.mjt.wish.list.auth.server;

import bg.sofia.uni.fmi.mjt.wish.list.server.AbstractServer;

import java.io.*;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class WishListAuthenticationServer extends AbstractServer { //da go napravq naslednik na server vmesto authserver
    private static final String REGISTER = "register";
    private static final String LOGIN = "login";
    private static final String LOGOUT = "logout";
    private static final String POST_WISH = "post-wish";
    private static final String GET_WISH = "get-wish";

    private static final String UNKNOWN_COMMAND_MSG = "[ Unknown command ]" + System.lineSeparator();
    static final String CONNECTION_PROBLEM_MESSAGE = "[ There is a problem with the server connection ]" + System.lineSeparator(); //da go opravq

    public WishListAuthenticationServer(int serverPort) {
        super(serverPort);
    }

    @Override
    public void start() {
        super.start();
        runServer();
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
                        } catch (IOException exception) {
                            continue;
                        }
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
            Integer sessionId = Extractor.extractSessionId(request);
            String command = Extractor.extractCommand(request);
            User user;
            switch (command) {
                case REGISTER:
                    user = new User(Extractor.extractName(request), Extractor.extractPassword(request));
                    return UsersRepository.register(sessionId, user);
                case LOGIN:
                    user = new User(Extractor.extractName(request), Extractor.extractPassword(request));
                    return UsersRepository.login(sessionId, user);
                case LOGOUT:
                    return UsersRepository.logout(sessionId);
                case POST_WISH:
                    user = new User(Extractor.extractName(request), Extractor.extractPassword(request));
                    return UsersRepository.allowWishlistSubmition(sessionId, user);
                case GET_WISH:
                    return UsersRepository.allowWishlistRetrieval(sessionId);
                default:
                    return UNKNOWN_COMMAND_MSG;
            }
        } catch (NotEnoughArgumentsException exception) {
            return exception.getMessage();
        }
    }

//    @Override
//    public void run() {
//        startServer();
//    }
}
