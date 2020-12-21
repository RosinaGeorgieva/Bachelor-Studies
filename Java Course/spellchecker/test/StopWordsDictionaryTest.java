import dictionaries.Dictionary;
import dictionaries.DictionaryType;
import dictionaries.MainDictionary;
import dictionaries.StopWordsDictionary;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.Set;

import static org.junit.Assert.*;

//ne minavat
public class StopWordsDictionaryTest {//vj za non-alphanumeric
    private static final String SAMPLE_DICTIONARY = """
            A
            $$you
            you're
            too
            """;

    private static final String CONTAINING_WORD = "a";
    private static final String NON_CONTAINING_WORD = "youd";

    private static final Set<String> EXPECTED_WORDS = Set.of("a", "you", "you're", "too");
    private static final Reader INPUT_READER = new StringReader(SAMPLE_DICTIONARY);
    private static Dictionary dictionary;

    @BeforeClass
    public static void setUp(){
        dictionary = new StopWordsDictionary(INPUT_READER, DictionaryType.STOP_WORDS_DICTIONARY);
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
