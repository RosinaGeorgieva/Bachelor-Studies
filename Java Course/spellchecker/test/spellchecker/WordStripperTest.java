package spellchecker;

import org.junit.Test;
import util.WordStripper;

import static org.junit.Assert.assertEquals;

public class WordStripperTest { //da dobavq suobshteniq
    private static String INPUT_1 = "    hello \n";
    private static String EXPECTED_1 = "hello";

    private static String INPUT_2 = "HelLO";
    private static String EXPECTED_2 = "hello";

    private static String INPUT_3 = "::hello!";
    private static String EXPECTED_3 = "hello";

    private static String INPUT_4 = "he&&llo";
    private static String EXPECTED_4 = INPUT_4;

    private static String INPUT_5 = "   he  ll o";
    private static String EXPECTED_5 = "he  ll o";

    @Test
    public void testRemoveBorderWhitespacesInputWithWhitespaces(){ //da dobavq oshte testowe za drugi case-ove?
        assertEquals(EXPECTED_1, WordStripper.removeBorderWhitespaces(INPUT_1));
    }

    @Test
    public void testMakeCaseInsensitiveWithMixedCase(){
        assertEquals(EXPECTED_2, WordStripper.makeCaseInsensitive(INPUT_2));
    }

    @Test
    public void testRemoveBorderNonalphanumeric(){
        assertEquals(EXPECTED_3, WordStripper.removeBorderNonAlphanumeric(INPUT_3));
    }

    @Test
    public void testRemoveBorderWhitespacesInputWithoutWhitespaces(){
        assertEquals(EXPECTED_4, WordStripper.removeBorderWhitespaces(INPUT_4));
    }

    @Test
    public void testRemoveBorderNonalphanumericWithNonBorder(){
        assertEquals(EXPECTED_5, WordStripper.removeBorderNonAlphanumeric(INPUT_5));
    }
}
