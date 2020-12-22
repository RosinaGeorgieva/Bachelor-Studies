import org.junit.Test;
import wordcleanser.WordNormalizer;

import static org.junit.Assert.assertEquals;

public class WordNormalizerTest { //da dobavq suobshteniq
    private static String INPUT_WITH_BORDER_WHITESPACES = "    hello \n";
    private static String REMOVED_BORDER_WHITESPACES = "hello";

    private static String INPUT_WITH_MIXED_CASE = "HelLO";
    private static String CASE_INSENSITIVE = "hello";

    private static String INPUT_WITH_BORDER_ALPHANUMERIC = "::hello!";
    private static String REMOVED_BORDER_ALPHANUMERIC = "hello";

    private static String INPUT_WITH_NON_BORDER_ALPHANUMERIC = "he&&llo";
    private static String NON_BORDER_ALPHANUMERIC = INPUT_WITH_NON_BORDER_ALPHANUMERIC;

//    private static String INPUT_WITH_INTERNAL_WHITESPACES = "   he  ll \n o";
//    private static String REMOVED_INTERNAL_WHITESPACES_EXPECTED = "hello";

    @Test
    public void testRemoveBorderWhitespacesInputWithWhitespaces(){ //da dobavq oshte testowe za drugi case-ove?
        assertEquals(REMOVED_BORDER_WHITESPACES, WordNormalizer.removeBorderWhitespaces(INPUT_WITH_BORDER_WHITESPACES));
    }

//    @Test
//    public void testRemoveBorderWhitespacesInputWithoutWhitespaces(){
//        assertEquals(REMOVED_BORDER_WHITESPACES, WordNormalizer.removeBorderWhitespaces(REMOVED_BORDER_WHITESPACES));
//    }

    @Test
    public void testMakeCaseInsensitiveWithMixedCase(){
        assertEquals(CASE_INSENSITIVE, WordNormalizer.makeCaseInsensitive(INPUT_WITH_MIXED_CASE));
    }

    @Test
    public void testRemoveBorderNonalphanumeric(){
        assertEquals(REMOVED_BORDER_ALPHANUMERIC, WordNormalizer.removeBorderNonAlphanumeric(INPUT_WITH_BORDER_ALPHANUMERIC));
    }

//    @Test
//    public void testRemoveBorderNonalphanumericWithNonBorder(){
//        assertEquals(NON_BORDER_ALPHANUMERIC, WordNormalizer.removeBorderNonAlphanumeric(INPUT_WITH_NON_BORDER_ALPHANUMERIC));
//    }

//    @Test tova ne mu e mqstoto v word normalizer
//    public void testRemoveAllWhitespaces(){
//        assertArrayEquals();
//    }
}
