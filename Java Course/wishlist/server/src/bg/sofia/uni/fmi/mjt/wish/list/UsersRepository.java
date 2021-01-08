package bg.sofia.uni.fmi.mjt.wish.list;

import java.util.HashMap;
import java.util.Map;

public class UsersRepository { //imenata na message_format-ite
    private static final String INVALID_USERNAME_MESSAGE_FORMAT = "[ Username %s is invalid, select a valid one ]%s";
    private static final String USERNAME_ALREADY_TAKEN_MESSAGE_FORMAT = "[ Username %s is already taken, select another one ]%s";
    private static final String SUCCESSFULLY_REGISTERED_MESSAGE_FORMAT = "[ Username %s successfully registered ]%s";
    private static final String SUCCESSFUL_LOGIN = "[ User %s successfully logged in ]%s";
    private static final String UNSUCCESSFUL_LOGIN = "[ Invalid username/password combination ]%s";

    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final Map<String, String> PASSWORD_BY_USERS = new HashMap<>();

    public static String register(String username, String password) {
        if(!Validator.isValidUsername(username)) {
            return String.format(INVALID_USERNAME_MESSAGE_FORMAT, username, LINE_SEPARATOR);
        }

        if(PASSWORD_BY_USERS.containsKey(username)) {
            return String.format(USERNAME_ALREADY_TAKEN_MESSAGE_FORMAT, username, LINE_SEPARATOR);
        } else {
            PASSWORD_BY_USERS.put(username, password);
            return String.format(SUCCESSFULLY_REGISTERED_MESSAGE_FORMAT, username, LINE_SEPARATOR);
        }
    }

    public static String login(String username, String password) {
        if(!PASSWORD_BY_USERS.containsKey(username) || !(PASSWORD_BY_USERS.get(username) .equals(password))) {
            return String.format(UNSUCCESSFUL_LOGIN, LINE_SEPARATOR);
        }
        return String.format(SUCCESSFUL_LOGIN, LINE_SEPARATOR);
    }
}
