package bg.sofia.uni.fmi.mjt.wish.list.server.command;

import bg.sofia.uni.fmi.mjt.wish.list.server.exception.UnknownCommandException;

import java.util.Arrays;
import java.util.Set;

public class CommandCreator {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String SPACE = " ";
    private static final String EMPTY_STRING = "";
    private static final String UKNOWN_COMMAND_MESSAGE = "[ Unknown command ]" + LINE_SEPARATOR;
    private static final Set<String> POSSIBLE_COMMANDS = Set.of("register", "login", "logout", "post-wish", "get-wish", "disconnect");

    public static Command newCommand(String request) throws UnknownCommandException {
        Integer sessionId = Integer.valueOf(extractIthWordFrom(request, 0));
        String command = extractIthWordFrom(request, 1);
        if(!POSSIBLE_COMMANDS.contains(command)) {
            throw new UnknownCommandException(UKNOWN_COMMAND_MESSAGE);
        }
        String[] allWords = request.replaceAll(LINE_SEPARATOR, EMPTY_STRING).split(SPACE);
        String[] arguments = Arrays.copyOfRange(allWords, 2, allWords.length);

        return new Command(sessionId, command, arguments);
    }

    private static String extractIthWordFrom(String request, int i) {
        return request.replaceAll(LINE_SEPARATOR, EMPTY_STRING).split(SPACE)[i];
    }
}
