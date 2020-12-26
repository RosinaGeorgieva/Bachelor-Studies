package spellchecker;

import dictionaries.AbstractDictionary;
import dictionaries.BasicDictionary;
import dictionaries.DictionaryType;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class BasicDictionaryTest {
    private static final String INPUT = """
                hello
            %%world
            mountain
            cat^^
            &&
            a
            Amazing
            I
            """;
    private static final Set<String> EXPECTED = Set.of("hello", "world", "mountain", "cat", "amazing");
    private static final String WORD_FROM_DICTIONARY = "hello";
    private static final String WORD_NOT_FROM_DICTIONARY = "hallo";
    private static final String EMPTY_WORD = "";

    private static AbstractDictionary dictionary;

    @Before
    public void setUp(){
        dictionary = new BasicDictionary(INPUT, DictionaryType.BASIC_DICTIONARY);
    }

    @Test
    public void testCompose() {
        assertEquals(EXPECTED, dictionary.getWords());
    }

    @Test
    public void testContainsWithContainingWord() {
        assertTrue(dictionary.contains(WORD_FROM_DICTIONARY));
    }

    @Test
    public void testContainsEmptyWord() {
        assertFalse(dictionary.contains(EMPTY_WORD));
    }

    @Test
    public void testContainsWithNonContainingWord(){
        assertFalse(dictionary.contains(WORD_NOT_FROM_DICTIONARY));
    }
}
