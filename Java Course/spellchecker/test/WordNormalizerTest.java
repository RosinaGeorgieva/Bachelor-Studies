import org.junit.Test;
import wordcleanser.WordNormalizer;

import static org.junit.Assert.assertEquals;

public class WordNormalizerTest { //da dobavq suobshteniq
    private static String INPUT_WITH_BORDER_WHITESPACES = "    hello \n";
    private static String REMOVED_BORDER_WHITESPACES_EXPECTED = "hello";

    private static String INPUT_WITH_MIXED_CASE = "HelLO";
    private static String CASE_INSENSITIVE_EXPECTED = "hello";

    private static String INPUT_WITH_BORDER_ALPHANUMERIC = "::hello!";
    private static String REMOVED_BORDER_ALPHANUMERIC_EXPECTED = "hello";

//    private static String INPUT_WITH_INTERNAL_WHITESPACES = "   he  ll \n o";
//    private static String REMOVED_INTERNAL_WHITESPACES_EXPECTED = "hello";

    @Test
    public void testRemoveBorderWhitespacesInputWithWhitespaces(){ //da dobavq oshte testowe za drugi case-ove?
        assertEquals(REMOVED_BORDER_WHITESPACES_EXPECTED, WordNormalizer.removeBorderWhitespaces(INPUT_WITH_BORDER_WHITESPACES));
    }

    @Test
    public void testMakeCaseInsensitiveWithMixedCase(){
        assertEquals(CASE_INSENSITIVE_EXPECTED, WordNormalizer.makeCaseInsensitive(INPUT_WITH_MIXED_CASE));
    }

    @Test
    public void testRemoveBorderNonalphanumeric(){
        assertEquals(REMOVED_BORDER_ALPHANUMERIC_EXPECTED, WordNormalizer.removeBorderNonAlphanumeric(INPUT_WITH_BORDER_ALPHANUMERIC));
    }

//    @Test tova ne mu e mqstoto v word normalizer
//    public void testRemoveAllWhitespaces(){
//        assertArrayEquals();
//    }
}
