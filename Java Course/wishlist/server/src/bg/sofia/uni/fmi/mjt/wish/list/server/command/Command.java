package bg.sofia.uni.fmi.mjt.wish.list.server.command;

import java.util.Arrays;
import java.util.stream.Collectors;

public record Command(Integer sessionId, String command, String[] arguments) {
    private static final String SPACE = " ";
    private static final String LINE_SEPARATOR = System.lineSeparator();

    public String toRequest() {
        String argumentsString = Arrays.stream(arguments()).collect(Collectors.joining(SPACE)); //MOJE DA GURMI OT TUK
        return String.format("%d %s %s %s", sessionId, command, argumentsString, LINE_SEPARATOR);
    }
}
