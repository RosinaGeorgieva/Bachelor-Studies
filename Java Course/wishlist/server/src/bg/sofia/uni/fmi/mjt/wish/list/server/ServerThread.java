package bg.sofia.uni.fmi.mjt.wish.list.server;

public class ServerThread extends Thread {
    private Server server;

    public ServerThread(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        server.start();
    }
}
