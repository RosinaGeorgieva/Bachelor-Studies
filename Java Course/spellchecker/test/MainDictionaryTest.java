import dictionaries.Dictionary;
import dictionaries.MainDictionary;
import dictionaries.DictionaryType;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.util.Set;

import static org.junit.Assert.*;

public class MainDictionaryTest {
    private static final String SAMPLE_DICTIONARY = """
                hello
            %%world
            mountain
            cat^^
            a
            Amazing
            I
            """;

    private static final String CONTAINING_WORD = "hello";
    private static final String NON_CONTAINING_WORD = "hallo";

    private static final Set<String> EXPECTED_WORDS = Set.of("hello", "world", "mountain", "cat", "amazing");
    private static final Reader INPUT_READER = new StringReader(SAMPLE_DICTIONARY);

    private static Dictionary dictionary;

    @BeforeClass
    public static void setUp(){
        dictionary = new MainDictionary(INPUT_READER, DictionaryType.MAIN_DICTIONARY);
    }

    @Test
    public void testCompose() {
        assertEquals(EXPECTED_WORDS, dictionary.getWords());
    }

    @Test
    public void testContainsWithContainingWord() {
        assertTrue(dictionary.contains(CONTAINING_WORD));
    }

    @Test
    public void testContainsWithNonContainingWord(){
        assertFalse(dictionary.contains(NON_CONTAINING_WORD));
    }
}
