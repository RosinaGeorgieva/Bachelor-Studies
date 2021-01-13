package bg.sofia.uni.fmi.mjt.wish.list.server.command;

import bg.sofia.uni.fmi.mjt.wish.list.server.exception.NotEnoughArgumentsException;
import bg.sofia.uni.fmi.mjt.wish.list.server.pojo.User;
import bg.sofia.uni.fmi.mjt.wish.list.server.pojo.Wish;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandCreator {
    private static final String EXCEPTION_MESSAGE = "[ Too few arguments provided. ]";
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String SPACE = " ";
    private static final String EMPTY_STRING = "";

    public static Command newCommand(String request) {
        Integer sessionId = Integer.valueOf(extractIthWordFrom(request, 0));
        String command = extractIthWordFrom(request, 1);
        String[] allWords = request.replaceAll(LINE_SEPARATOR, EMPTY_STRING).split(SPACE);
        String[] arguments = Arrays.copyOfRange(allWords, 2, allWords.length);

        return new Command(sessionId, command, arguments);
    }

    private static String extractIthWordFrom(String request, int i) throws NotEnoughArgumentsException {
        String[] allWords = request.replaceAll(LINE_SEPARATOR, EMPTY_STRING).split(SPACE);
        if (allWords.length <= i) {
            throw new NotEnoughArgumentsException(EXCEPTION_MESSAGE);
        }
        return request.replaceAll(LINE_SEPARATOR, EMPTY_STRING).split(SPACE)[i];
    }
}
