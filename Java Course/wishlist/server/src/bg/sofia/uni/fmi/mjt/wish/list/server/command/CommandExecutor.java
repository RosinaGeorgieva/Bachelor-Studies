package bg.sofia.uni.fmi.mjt.wish.list.server.command;

import bg.sofia.uni.fmi.mjt.wish.list.client.AbstractClient;
import bg.sofia.uni.fmi.mjt.wish.list.server.exception.NoUsersAvailableException;
import bg.sofia.uni.fmi.mjt.wish.list.server.pojo.User;
import bg.sofia.uni.fmi.mjt.wish.list.server.pojo.Wish;
import bg.sofia.uni.fmi.mjt.wish.list.server.repository.Repository;
import bg.sofia.uni.fmi.mjt.wish.list.server.repository.SessionsRepository;
import bg.sofia.uni.fmi.mjt.wish.list.server.repository.WishesRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class CommandExecutor extends AbstractClient {
    private static final String NO_STUDENTS_IN_LIST_MESSAGE_FORMAT = "[ There are no students present in the wish list ]%s";
    private static final String WISH_LIST_FORMAT = "[ %s: %s ]%s";
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String UKNOWN_COMMAND_MESSAGE = "[ Unknown command ]" + LINE_SEPARATOR;
    private static final String DISCONNECTED_FROM_SERVER = "[ Disconnected from server ]" + LINE_SEPARATOR;
    private static final String REGISTER = "register";
    private static final String LOGIN = "login";
    private static final String LOGOUT = "logout";
    private static final String POST_WISH = "post-wish";
    private static final String GET_WISH = "get-wish";
    private static final String DISCONNECT = "disconnect";

    private static final String OK = "-*&OK&*-" + LINE_SEPARATOR;

    private final Repository wishesRepository;
    private final Repository sessionsRepository;

    public CommandExecutor(int serverPort) {
        super(serverPort);
        this.wishesRepository = new WishesRepository();
        this.sessionsRepository = new SessionsRepository();
    }

    public CommandExecutor(Repository wishesRepository, Repository sessionsRepository) {
        this.wishesRepository = wishesRepository;
        this.sessionsRepository = sessionsRepository;
    }

    public String execute(Command command) {
        String request = command.toRequest();
        String authResponse = requestAuthentication(request);
        return executeAuthenticated(authResponse, command);
    }

    public String executeAuthenticated(String authResponse, Command command) {
        return switch (command.command()) {
            case REGISTER -> register(authResponse, command);
            case LOGIN -> login(authResponse, command);
            case LOGOUT -> logout(authResponse, command);
            case POST_WISH -> postWish(authResponse, command);
            case GET_WISH -> getWish(authResponse, command);
            case DISCONNECT -> disconnect(command);
            default -> UKNOWN_COMMAND_MESSAGE;
        };
    }

    private String disconnect(Command command) {
        sessionsRepository.remove(command.sessionId());
        return DISCONNECTED_FROM_SERVER;
    }

    private String getWish(String authResponse, Command command) { //TODO eventualno da mahna tazi logika ot tuk
        if(!authResponse.equals(OK)){
            return authResponse;
        }

        if (((WishesRepository)wishesRepository).getAllEntries().size() == 0) {
            return String.format(NO_STUDENTS_IN_LIST_MESSAGE_FORMAT, LINE_SEPARATOR);
        }
        try {
            User chosenStudent = randomGiftRecipientFor(((SessionsRepository)sessionsRepository).get(command.sessionId()));
            Collection<Wish> wishList = ((WishesRepository)wishesRepository).get(chosenStudent);
            String wishListString = String.format(WISH_LIST_FORMAT, chosenStudent, wishList.toString(), LINE_SEPARATOR);;
            wishesRepository.remove(chosenStudent);
            return wishListString;
        } catch (NoUsersAvailableException exception) {
            return String.format(NO_STUDENTS_IN_LIST_MESSAGE_FORMAT, LINE_SEPARATOR);
        }
    }

    private String postWish(String authResponse, Command command) { //ima vid post-wish rosi kukla -> args[0] e koi
        if(!authResponse.equals(OK)){
            return authResponse;
        }
        String[] wishDescription = Arrays.copyOfRange(command.arguments(), 1, command.arguments().length);
        String wishDescriptionString = Arrays.stream(wishDescription).collect(Collectors.joining(" ")); //da go sloja tuk da ne e sapce
        return wishesRepository.add(new User(command.arguments()[0]), new Wish(wishDescriptionString));
    }

    private String logout(String authResponse, Command command) {
        sessionsRepository.remove(command.sessionId());
        return authResponse;
    }

    private String login(String authResponse, Command command) {
        sessionsRepository.add(command.sessionId(), new User(command.arguments()[0]));
        return authResponse;
    }

    private String register(String authResponse, Command command) {
        sessionsRepository.add(command.sessionId(), new User(command.arguments()[0]));
        return authResponse;
    }

    private String requestAuthentication(String request) {
        sendRequest(request);
        return receiveResponse();
    }

    private User randomGiftRecipientFor(User user) throws NoUsersAvailableException {
        List<User> users = new ArrayList<>(((WishesRepository)wishesRepository).getAllEntries().keySet());
        if (users.size() == 1 && users.contains(user)) {
            throw new NoUsersAvailableException(NO_STUDENTS_IN_LIST_MESSAGE_FORMAT);
        }

        int randomNumber = ThreadLocalRandom.current().nextInt(0, ((WishesRepository)wishesRepository).getAllEntries().size());
        User randomUser = users.get(randomNumber);

        return randomUser;
    }
}
