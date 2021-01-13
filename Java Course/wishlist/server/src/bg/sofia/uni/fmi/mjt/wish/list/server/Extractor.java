package bg.sofia.uni.fmi.mjt.wish.list.server;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Extractor {
    private static final String EXCEPTION_MESSAGE = "[ Too few arguments provided. ]";
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String SPACE = " ";//da pogledna za s+
    private static final String EMPTY_STRING = "";

    public static Integer extractSessionId(String request) throws NotEnoughArgumentsException {
        return Integer.valueOf(extractIthWordFrom(request, 0));
    }

    public static String extractCommand(String request) throws NotEnoughArgumentsException {
        return extractIthWordFrom(request, 1);
    }

    public static User extractUser(String request) throws NotEnoughArgumentsException {
        return new User(extractIthWordFrom(request, 2));
    }

    public static Wish extractWish(String request) throws NotEnoughArgumentsException {
        String[] allWords = request.replaceAll(LINE_SEPARATOR, EMPTY_STRING).split(SPACE);;
        String[] wishDescription = Arrays.copyOfRange(allWords, 3, allWords.length);
//        int firstOccurenceOfSpace = request.indexOf(SPACE);
//        int secondOccurenceOfSpace = request.indexOf(SPACE, firstOccurenceOfSpace + 1);
//        if(secondOccurenceOfSpace == -1) {
//            throw  new NotEnoughArgumentsException(EXCEPTION_MESSAGE);
//        }
        return new Wish(Arrays.stream(wishDescription).collect(Collectors.joining(SPACE)));
    }

    private static String extractIthWordFrom(String request, int i) throws NotEnoughArgumentsException {
        String[] allWords = request.replaceAll(LINE_SEPARATOR, EMPTY_STRING).split(SPACE);
        if (allWords.length <= i) {
            throw new NotEnoughArgumentsException(EXCEPTION_MESSAGE);
        }
        return request.replaceAll(LINE_SEPARATOR, EMPTY_STRING).split(SPACE)[i];
    }
}
