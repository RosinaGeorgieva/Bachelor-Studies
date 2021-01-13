package bg.sofia.uni.fmi.mjt.wish.list;

import bg.sofia.uni.fmi.mjt.wish.list.server.ServerThread;
import bg.sofia.uni.fmi.mjt.wish.list.server.WishListServer;
import bg.sofia.uni.fmi.mjt.wish.list.server.command.CommandCreator;

public class App {
    public static void main(String[] args) {
//        System.out.println(CommandCreator.newCommand("2 disconnect"));
        WishListServer server = new WishListServer(7777);

        new ServerThread(server).start();
    }
}
