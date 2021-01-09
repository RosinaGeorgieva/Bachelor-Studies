public class Extractor {
    private static final String EXCEPTION_MESSAGE = "[ Too few arguments provided ]";
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String SPACE = " ";//da pogledna za s+
    private static final String EMPTY_STRING = "";

    //DA SE POGRIJA ZA AKO LIPSVA NQKOI AGRUMENT PRI KOMANDA
    public static String extractClientId(String request) {
        try {
            return extractIthWordFrom(request, 0);
        } catch (NotEnoughArgumentsException exception) {
            return exception.getMessage();
        }
    }

    public static String extractCommand(String request) throws NotEnoughArgumentsException {
        return extractIthWordFrom(request, 1);
    }

    public static String extractName(String request) throws NotEnoughArgumentsException {
        return extractIthWordFrom(request, 2);
    }

    public static String extractPassword(String request) throws NotEnoughArgumentsException {
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
