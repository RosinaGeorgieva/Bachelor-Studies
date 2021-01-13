package bg.sofia.uni.fmi.mjt.wish.list.server;

import bg.sofia.uni.fmi.mjt.wish.list.auth.server.WishListAuthenticationServer;
import bg.sofia.uni.fmi.mjt.wish.list.server.command.Command;
import bg.sofia.uni.fmi.mjt.wish.list.server.command.CommandCreator;
import bg.sofia.uni.fmi.mjt.wish.list.server.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.wish.list.server.exception.ClientDisconnectedException;
import bg.sofia.uni.fmi.mjt.wish.list.server.exception.NotEnoughArgumentsException;

import java.io.*;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class WishListServer extends AbstractServer {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String AUTH_REQUEST_FORMAT = "%d %s";

    private CommandExecutor commandExecutor;
    private int authServerPort;
    private Server authenticationServer;

    public WishListServer(int serverPort) {
        super(serverPort);
        this.authServerPort = this.serverPort + 1;
        this.authenticationServer = new WishListAuthenticationServer(this.authServerPort);
        this.commandExecutor = new CommandExecutor(this.authServerPort);
    }

    @Override
    public void start() {
        //da ne zabravq da spiram v stop drugiq server; N.B. DA OGLEDAM DOBRE STOP; VEROQTNO SHTE ISKA PREDEFINICIQ
        new ServerThread(authenticationServer).start();
        commandExecutor.connect();

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
                        } catch (ClientDisconnectedException exception) { //da vidq kakwo stava kato se disconnect-nat vsichki
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
            Command command = CommandCreator.newCommand(request);
            return commandExecutor.execute(command);
        } catch (NotEnoughArgumentsException exception) { //Zashto se catch-va? Da se throwne?? kato runtime?
            exception.printStackTrace();
            return exception.getMessage();
        }
    }
}
