import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class WishListAuthServer {
    private static final int SERVER_PORT = 7778;
    private static final int BUFFER_SIZE = 1024;

    private static final String REGISTER = "register";
    private static final String LOGIN = "login";
    private static final String LOGOUT = "logout";
    private static final String POST_WISH = "post-wish";
    private static final String GET_WISH = "get-wish";

    public static void main(String[] args) {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {

            serverSocketChannel.bind(new InetSocketAddress("localhost", SERVER_PORT));
            serverSocketChannel.configureBlocking(false);

            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

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
                            System.out.println("Client has closed the connection");
                            sc.close();
                            continue;
                        }

                        buffer.flip();
                        String request = new String(buffer.array(), 0, buffer.limit());

                        buffer.clear();

                        Integer clientId = Integer.parseInt(Extractor.extractSessionId(request));
                        String response = respondTo(clientId, request);
                        buffer.put(response.getBytes(StandardCharsets.UTF_8));

                        buffer.flip();
                        sc.write(buffer);

                    } else if (key.isAcceptable()) {
                        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
                        SocketChannel accept = sockChannel.accept();
                        accept.configureBlocking(false);
                        accept.register(selector, SelectionKey.OP_READ);
                    }

                    keyIterator.remove();
                }

            }
        } catch (IOException | NotEnoughArgumentsException e) {
            System.out.println("[ There is a problem with the server connection ]");
        }
    }

    private static String respondTo(Integer sessionId, String request) {
        try {
            String command = Extractor.extractCommand(request);
            User user = null;
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
                    return "[ Unknown command ]" + System.lineSeparator();
            }
        } catch (NotEnoughArgumentsException exception) {
            return exception.getMessage() + System.lineSeparator();
        }
    }
}
