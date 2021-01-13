package bg.sofia.uni.fmi.mjt.wish.list.auth.server;

public class Extractor {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String SPACE = " ";//da pogledna za s+
    private static final String EMPTY_STRING = "";
    private static final String EXCEPTION_MESSAGE = "[ Too few arguments provided. ]" + LINE_SEPARATOR;

    public static Integer extractSessionId(String request) throws NotEnoughArgumentsException {
        return Integer.valueOf(extractIthWordFrom(request, 0));
    }

    public static String extractCommand(String request) throws NotEnoughArgumentsException {
        return extractIthWordFrom(request, 1);
    }

    public static String extractName(String request) throws NotEnoughArgumentsException {
        return extractIthWordFrom(request, 2);
    }

    public static String extractPassword(String request) throws NotEnoughArgumentsException { //ako ima parola e na 2 mqsto;
        return extractIthWordFrom(request, 3);
    }

    private static String extractIthWordFrom(String request, int i) throws NotEnoughArgumentsException {
        String[] allWords = request.replaceAll(LINE_SEPARATOR, EMPTY_STRING).split(SPACE);
        if (allWords.length <= i) {
            throw new NotEnoughArgumentsException(EXCEPTION_MESSAGE);
        }
        return request.replaceAll(LINE_SEPARATOR, EMPTY_STRING).split(SPACE)[i];
    }
}
