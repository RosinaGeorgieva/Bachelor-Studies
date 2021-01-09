import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
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

                        Integer clientId = Integer.parseInt(Extractor.extractClientId(request));
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
        } catch (IOException e) {
            System.out.println("[ There is a problem with the server connection ]");
        }
    }

    private static String respondTo(Integer clientId, String request) {
        try {
            CommandType command = deduceType(Extractor.extractCommand(request));
            switch (command) {
                case REGISTER:
                    return UsersRepository.register(clientId, Extractor.extractName(request), Extractor.extractPassword(request));
                case LOGIN:
                    return UsersRepository.login(clientId, Extractor.extractName(request), Extractor.extractPassword(request));
                case LOGOUT:
                    return UsersRepository.logout(clientId);
                case CHECK_LOGGED_IN:
                    return UsersRepository.checkLoggedIn(clientId);
                case CHECK_EXISTING_USER:
                    return UsersRepository.checkExistingUser(clientId, Extractor.extractName(request));
                default:
                    return "[ Unknown command ]" + System.lineSeparator();
            }
        } catch (NotEnoughArgumentsException exception) {
            return exception.getMessage() + System.lineSeparator();
        }
    }

    public static CommandType deduceType(String command) {
        if(command.equals("register")) {
            return CommandType.REGISTER;
        }
        if(command.equals("login")) {
            return CommandType.LOGIN;
        }
        if(command.equals("logout")) {
            return CommandType.LOGOUT;
        }
        if(command.equals("post-wish")) {
            return CommandType.CHECK_EXISTING_USER;
        }
        if(command.equals("get-wish")) {
            return CommandType.CHECK_LOGGED_IN;
        }
        return CommandType.INVALID_COMMAND;
    }
}
