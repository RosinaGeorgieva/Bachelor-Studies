package bg.sofia.uni.fmi.mjt.wish.list;

import bg.sofia.uni.fmi.mjt.wish.list.server.ServerThread;
import bg.sofia.uni.fmi.mjt.wish.list.server.WishListServer;

public class App {
    public static void main(String[] args) {
        WishListServer server = new WishListServer(7777);

        new ServerThread(server).start();
        System.out.println("tukaaa");
    }
}
