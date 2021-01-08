package bg.sofia.uni.fmi.mjt.wish.list;

import java.util.regex.Pattern;

public class Validator {
    private static final String ALLOWED_SYMBOLS_REGEX = "[a-zA-Z0-9\\-\\.\\_]+";

    public static boolean isValidUsername(String username) {
        return Pattern.compile(ALLOWED_SYMBOLS_REGEX).matcher(username).matches();
    }
}
