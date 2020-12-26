package spellchecker;

import org.junit.Test;
import util.SentenceDivider;

import static org.junit.Assert.assertArrayEquals;

public class SentenceDividerTest {
    private static final String INPUT_1 = "Hello there budy";
    private static final String[] EXPECTED_1 = {"Hello", "there", "budy"};

    @Test
    public void testRemoveAllWhitespaces(){
        assertArrayEquals(EXPECTED_1, SentenceDivider.divide(INPUT_1));
    }
}
