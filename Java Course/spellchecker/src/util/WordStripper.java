package util;

public class WordStripper {
    private static final String NON_ALPHANUMERIC_REGEX = "^[^a-zA-Z0-9]+|[^a-zA-Z0-9]+$";
    private static final String EMPTY_WORD = "";

    public static String strip(String word) {
        word = WordStripper.makeCaseInsensitive(word);
        word = WordStripper.removeBorderWhitespaces(word);
        return WordStripper.removeBorderNonAlphanumeric(word);
    }

    public static String makeCaseInsensitive(String text) {
        return text.toLowerCase();
    }

    public static String removeBorderWhitespaces(String text) {
        return text.trim();
    }

    public static String removeBorderNonAlphanumeric(String text) {
        return text.replaceAll(NON_ALPHANUMERIC_REGEX, EMPTY_WORD);
    }
}
