import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UsersRepository { //imenata na message_format-ite
    private static final String INVALID_USERNAME_MESSAGE_FORMAT = "[ Username %s is invalid, select a valid one ]%s";
    private static final String USERNAME_ALREADY_TAKEN_MESSAGE_FORMAT = "[ Username %s is already taken, select another one ]%s";
    private static final String SUCCESSFULLY_REGISTERED_MESSAGE_FORMAT = "[ Username %s successfully registered ]%s";
    private static final String SUCCESSFUL_LOGIN = "[ User %s successfully logged in ]%s";
    private static final String UNSUCCESSFUL_LOGIN = "[ Invalid username/password combination ]%s";
    private static final String LOGOUT = "[ Successfully logged out ]%s";
    private static final String NOT_LOGGED_IN = "[ You are not logged in ]%s";
    private static final String ALREADY_LOGGED_IN = "[ You are already logged in]%s";
    private static final String NO_SUCH_REGISTERED = "[ Student with username %s is not registered ]%s";
    private static final String OK = "-*&OK&*-";

    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final Map<String, String> PASSWORD_BY_USERS = new HashMap<>();
    private static final Set<Integer> LOGGED_CLIENTS = new HashSet<>(); //set ili list??

    public static String register(Integer clientId, String username, String password) {
        if (!Validator.isValidUsername(username)) {
            return String.format(INVALID_USERNAME_MESSAGE_FORMAT, username, LINE_SEPARATOR);
        }

        if (PASSWORD_BY_USERS.containsKey(username)) {
            return String.format(USERNAME_ALREADY_TAKEN_MESSAGE_FORMAT, username, LINE_SEPARATOR);
        } else {
            PASSWORD_BY_USERS.put(username, password);
            LOGGED_CLIENTS.add(clientId);
            return String.format(SUCCESSFULLY_REGISTERED_MESSAGE_FORMAT, username, LINE_SEPARATOR);
        }
    }

    public static String login(Integer clientId, String username, String password) {
        if (!PASSWORD_BY_USERS.containsKey(username) || !(PASSWORD_BY_USERS.get(username).equals(password))) {
            return String.format(UNSUCCESSFUL_LOGIN, LINE_SEPARATOR);
        }
        if (LOGGED_CLIENTS.contains(clientId)) {
            return String.format(ALREADY_LOGGED_IN, LINE_SEPARATOR);
        }
        LOGGED_CLIENTS.add(clientId);
        return String.format(SUCCESSFUL_LOGIN, username, LINE_SEPARATOR);
    }

    public static String logout(Integer clientId) {
        if (!LOGGED_CLIENTS.contains(clientId)) {
            return String.format(NOT_LOGGED_IN, LINE_SEPARATOR);
        }
        LOGGED_CLIENTS.remove(clientId);
        return String.format(LOGOUT, LINE_SEPARATOR);
    }

    public static String checkLoggedIn(Integer clientId) {
        if (!LOGGED_CLIENTS.contains(clientId)) {
            return String.format(NOT_LOGGED_IN, LINE_SEPARATOR);
        }
        return OK + LINE_SEPARATOR;
    }

    public static String checkExistingUser(Integer whoChecks, String user) {
        if (!LOGGED_CLIENTS.contains(whoChecks)) {
            return String.format(NOT_LOGGED_IN, LINE_SEPARATOR);
        }
        if (!PASSWORD_BY_USERS.containsKey(user)) {
            return String.format(NO_SUCH_REGISTERED, user, LINE_SEPARATOR);
        }
        return OK + LINE_SEPARATOR;
    }
}
