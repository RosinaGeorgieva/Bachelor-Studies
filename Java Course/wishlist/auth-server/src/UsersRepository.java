import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UsersRepository { //imenata na message_format-ite
    private static final String INVALID_USERNAME_MSG = "[ Username %s is invalid, select a valid one ]%s";
    private static final String USERNAME_ALREADY_TAKEN_MSG = "[ Username %s is already taken, select another one ]%s";
    private static final String SUCCESSFULLY_REGISTERED_MSG = "[ Username %s successfully registered ]%s";
    private static final String SUCCESSFUL_LOGIN_MSG = "[ User %s successfully logged in ]%s";
    private static final String ALREADY_LOGGED_IN_MSG = "[ You are already logged in]%s";
    private static final String UNSUCCESSFUL_LOGIN_MSG = "[ Invalid username/password combination ]%s";
    private static final String SUCCESSFUL_LOGOUT_MSG = "[ Successfully logged out ]%s";
    private static final String NOT_LOGGED_IN_MSG = "[ You are not logged in ]%s";
    private static final String NO_SUCH_REGISTERED_MSG = "[ Student with username %s is not registered ]%s";
    private static final String OK = "-*&OK&*-";
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private static final Set<User> users = new HashSet<>();
    private static final Set<Integer> sessionIds = new HashSet<>(); //set ili list??

    public static String register(Integer sessionId, User user) {
        String username = user.username(); // a parolata trqbwa li da ima nqkakwi iziskwaniq?

        if (!Validator.isValidUsername(username)) {
            return String.format(INVALID_USERNAME_MSG, username, LINE_SEPARATOR);
        }

        Set<String> allUsernames = users.stream().map(User::username).collect(Collectors.toSet());
        if (allUsernames.contains(username)) {
            return String.format(USERNAME_ALREADY_TAKEN_MSG, username, LINE_SEPARATOR);
        }

        users.add(user);
        sessionIds.add(sessionId);
        return String.format(SUCCESSFULLY_REGISTERED_MSG, username, LINE_SEPARATOR);
    }

    public static String login(Integer sessionId, User user) {
        if (!users.contains(user)) { //ot tuk event problem
            return String.format(UNSUCCESSFUL_LOGIN_MSG, LINE_SEPARATOR);
        }

        if (sessionIds.contains(sessionId)) {
            return String.format(ALREADY_LOGGED_IN_MSG, LINE_SEPARATOR);
        }

        sessionIds.add(sessionId);
        return String.format(SUCCESSFUL_LOGIN_MSG, user.username(), LINE_SEPARATOR);
    }

    public static String logout(Integer sessionId) {
        if (!sessionIds.contains(sessionId)) {
            return String.format(NOT_LOGGED_IN_MSG, LINE_SEPARATOR);
        }

        sessionIds.remove(sessionId);
        return String.format(SUCCESSFUL_LOGOUT_MSG, LINE_SEPARATOR);
    }

    public static String allowWishlistRetrieval(Integer sessionId) {
        if (!sessionIds.contains(sessionId)) {
            return String.format(NOT_LOGGED_IN_MSG, LINE_SEPARATOR);
        }

        return OK + LINE_SEPARATOR;
    }

    public static String allowWishlistSubmition(Integer sessionId, User user) {
        if (!sessionIds.contains(sessionId)) {
            return String.format(NOT_LOGGED_IN_MSG, LINE_SEPARATOR);
        }

        Set<String> allUsernames = users.stream().map(User::username).collect(Collectors.toSet());
        if (!allUsernames.contains(user.username())) {
            return String.format(NO_SUCH_REGISTERED_MSG, user.username(), LINE_SEPARATOR);
        }
        return OK + LINE_SEPARATOR;
    }
}
