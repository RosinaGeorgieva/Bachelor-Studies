package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SentenceDivider {
    private static final String WHITESPACE_REGEX = "\\s+";
    private static final String NEWLINE = System.lineSeparator();
    private static final String INTERVAL = " ";
    private static final String EMPTY_WORD = "";

    public static String[] divide(String text) {
        List<String> words = Arrays.stream(text.split(WHITESPACE_REGEX)).filter(word -> word.length() != 0).collect(Collectors.toList());
        return words.toArray(new String[0]);
    }
}
