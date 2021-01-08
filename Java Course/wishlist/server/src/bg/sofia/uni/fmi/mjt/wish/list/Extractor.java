package bg.sofia.uni.fmi.mjt.wish.list;

public class Extractor {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String SPACE = " ";//da pogledna za s+
    private static final String EMPTY_STRING = "";

    public static String extractCommand(String request) {
        return extractIthWordFrom(request, 0);
    }

    public static String extractName(String request) {
        return extractIthWordFrom(request, 1);
    }

    public static Wish extractWish(String request) {
        int firstOccurenceOfSpace = request.indexOf(SPACE);
        int secondOccurenceOfSpace = request.indexOf(SPACE, firstOccurenceOfSpace + 1);
        return new Wish(request.substring(secondOccurenceOfSpace + 1).replaceAll(LINE_SEPARATOR, EMPTY_STRING));
    }

    private static String extractIthWordFrom(String request, int i){
        return request.replaceAll(LINE_SEPARATOR, EMPTY_STRING).split(SPACE)[i];
    }
}
