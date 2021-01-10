package bg.sofia.uni.fmi.mjt.wish.list;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class WishListClient {
    private static final int SERVER_PORT = 7777;
    private static final String DISCONNECTED_FROM_SERVER = "[ Disconnected from server ]";

    public static void main(String[] args) { //da si izvadq konstantite w nqkakyw klas s message-i;

        try (SocketChannel socketChannel = SocketChannel.open();
             BufferedReader reader = new BufferedReader(Channels.newReader(socketChannel, "UTF-8"));
             PrintWriter writer = new PrintWriter(Channels.newWriter(socketChannel, "UTF-8"), true);
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress("localhost", SERVER_PORT));

            while (true) {
                System.out.print("=> ");
                String message = scanner.nextLine();

                writer.println(message);

                String reply = reader.readLine();
                writer.flush();

                System.out.println(reply);

                if(reply.equals(DISCONNECTED_FROM_SERVER)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("[ There is a problem with the server connection ]");
        }
    }
}
