package bg.sofia.uni.fmi.mjt.wish.list.server.command;

import java.util.Arrays;
import java.util.stream.Collectors;

public record Command(Integer sessionId, String command, String[] arguments) {
    private static final String SPACE = " ";
    private static final String LINE_SEPARATOR = System.lineSeparator();

    public String toRequest() {
        String argumentsString = Arrays.stream(arguments()).collect(Collectors.joining(SPACE));
        return String.format("%d %s %s %s", sessionId, command, argumentsString, LINE_SEPARATOR);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Command)) {
            return false;
        }
        Command c = (Command) o;
        return sessionId.equals(c.sessionId) && command.equals(c.command) && Arrays.equals(arguments, c.arguments);
    }
}
