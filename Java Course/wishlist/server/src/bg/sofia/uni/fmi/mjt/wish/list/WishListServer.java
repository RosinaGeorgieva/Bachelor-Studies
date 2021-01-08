package bg.sofia.uni.fmi.mjt.wish.list;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
//N.B. VSQKO SYOBSHTENIE DA E S LINE SEPARATOR
public class WishListServer {
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 1024;
    private static int serverPort = 7777; // DA MAHNA 7777??? static v konstruktor???
    private static final WishListRepository wishes = new WishListRepository();

//    public WishListServer(int serverPort) { // DA GO DOBAVQ!
//        serverPort = serverPort;
//        wishes = new StudentsWishes();
//    }

    public static void main(String[] args) {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {

            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, serverPort));
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
                        String response = respondTo(request);
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

        }
    }

    private static String respondTo(String request) {
        String command = Extractor.extractCommand(request);
        if (command.equals("post-wish")) {
            String name = Extractor.extractName(request);
            Wish wish = Extractor.extractWish(request);
            return wishes.postWish(name, wish);
        } else if (command.equals("get-wish")) {
            return wishes.getRandomWishList();
        } else if (command.equals("register") || command.equals("login")) {
            //stava klient na drug server koito e za authorizaciq
            //nashiqt server shte pita auth server-a dali ima takava kombinaciq;
            //ako mu vyrne da, shte si produljava rabotata tozi
            //inache, tozi shte vrushta
        }
        return "[ Unknown command ]" + System.lineSeparator();
    }
}