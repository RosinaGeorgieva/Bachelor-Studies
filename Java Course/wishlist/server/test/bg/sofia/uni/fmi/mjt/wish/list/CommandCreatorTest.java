package bg.sofia.uni.fmi.mjt.wish.list;

import bg.sofia.uni.fmi.mjt.wish.list.server.command.Command;
import bg.sofia.uni.fmi.mjt.wish.list.server.command.CommandCreator;
import bg.sofia.uni.fmi.mjt.wish.list.server.exception.UnknownCommandException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommandCreatorTest {
    private static final String REGISTER_ROSI = "3 register rosi 12";
    private static final String UNKNOWN_COMMAND = "1 ei sega shte hvurlq exception";

    @Test
    public void testNewValidCommand() throws UnknownCommandException {
        Command expected = new Command(3, "register", new String[]{"rosi", "12"});
        assertEquals(expected, CommandCreator.newCommand(REGISTER_ROSI));
    }

    @Test(expected = UnknownCommandException.class)
    public void testNewCommandNotEnoughArguments() throws UnknownCommandException {
        CommandCreator.newCommand(UNKNOWN_COMMAND);
    }
}
