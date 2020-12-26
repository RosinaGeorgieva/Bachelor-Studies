package spellchecker;

import dictionaries.AbstractDictionary;
import dictionaries.DictionaryType;
import dictionaries.StopWordsDictionary;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class StopWordsDictionaryTest {
    private static final String INPUT = """
            A
            $$you
            you're
            too
            """;
    private static final Set<String> EXPECTED = Set.of("a", "you", "you're", "too");
    private static final String WORD_FROM_DICTIONARY = "a";
    private static final String WORD_NOT_FROM_DICTIONARY = "youd";
    private static final String EMPTY_WORD = "";

    private static AbstractDictionary dictionary;

    @Before
    public void setUp(){
        dictionary = new StopWordsDictionary(INPUT, DictionaryType.STOP_WORDS_DICTIONARY);
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
    public void testContainsWithEmptyWord() {
        assertFalse(dictionary.contains(EMPTY_WORD));
    }

    @Test
    public void testContainsWithNonContainingWord(){
        assertFalse(dictionary.contains(WORD_NOT_FROM_DICTIONARY));
    }
}
