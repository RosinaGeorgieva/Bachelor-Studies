package spellchecker;

import dictionaries.BasicDictionary;
import dictionaries.StopWordsDictionary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import spellchecker.Metadata;
import spellchecker.MetadataGenerator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MetadataGeneratorTest {
    private static final String INPUT_1 = "Hallo, you!";
    private static final String MISTAKEN_WORD_1 = "hallo";
    private static final String STOP_WORD = "you";
    private static final int NUMBER_OF_CHARACTERS_INPUT_1 = 10;
    private static final int NUMBER_OF_WORDS_INPUT_1 = 1;
    private static final int NUMBER_OF_MISTAKES_INPUT_1 = 1;
    private static final Metadata EXPECTED_1 = new Metadata(NUMBER_OF_CHARACTERS_INPUT_1, NUMBER_OF_WORDS_INPUT_1, NUMBER_OF_MISTAKES_INPUT_1);

    private static final String INPUT_2 = "Haw are you, \n babi?";
    private static final String MISTAKEN_WORD_2 = "haw";
    private static final String MISTAKEN_WORD_3 = "babi";
    private static final String CORRECT_WORD_1 = "are";
    private static final int NUMBER_OF_CHARACTERS_INPUT_2 = 15;
    private static final int NUMBER_OF_WORDS_INPUT_2 = 3;
    private static final int NUMBER_OF_MISTAKES_INPUT_2 = 2;
    private static final Metadata EXPECTED_2 = new Metadata(NUMBER_OF_CHARACTERS_INPUT_2, NUMBER_OF_WORDS_INPUT_2, NUMBER_OF_MISTAKES_INPUT_2);

    private static final String INPUT_3 = "How are you, baby?";
    private static final String CORRECT_WORD_2 = "how";
    private static final String CORRECT_WORD_3 = "are";
    private static final String CORRECT_WORD_4 = "baby";
    private static final int NUMBER_OF_CHARACTERS_INPUT_3 = 15;
    private static final int NUMBER_OF_WORDS_INPUT_3 = 3;
    private static final int NUMBER_OF_MISTAKES_INPUT_3 = 0;
    private static final Metadata EXPECTED_3 = new Metadata(NUMBER_OF_CHARACTERS_INPUT_3, NUMBER_OF_WORDS_INPUT_3, NUMBER_OF_MISTAKES_INPUT_3);

    @Mock
    private BasicDictionary basicDictionary;

    @Mock
    private StopWordsDictionary stopWordsDictionary;

    @Test
    public void testGenerateWithSingleWrongWord() {
        when(basicDictionary.contains(MISTAKEN_WORD_1)).thenReturn(false);
        when(basicDictionary.contains(STOP_WORD)).thenReturn(false);
        when(stopWordsDictionary.contains(MISTAKEN_WORD_1)).thenReturn(false);
        when(stopWordsDictionary.contains(STOP_WORD)).thenReturn(true);

        assertEquals(EXPECTED_1, MetadataGenerator.generate(INPUT_1, basicDictionary, stopWordsDictionary));
    }

    @Test
    public void testGenerateWithMoreWrongWords() {
        when(basicDictionary.contains(MISTAKEN_WORD_2)).thenReturn(false);
        when(basicDictionary.contains(MISTAKEN_WORD_3)).thenReturn(false);
        when(basicDictionary.contains(CORRECT_WORD_1)).thenReturn(true);
        when(basicDictionary.contains(STOP_WORD)).thenReturn(false);
        when(stopWordsDictionary.contains(MISTAKEN_WORD_2)).thenReturn(false);
        when(stopWordsDictionary.contains(MISTAKEN_WORD_3)).thenReturn(false);
        when(stopWordsDictionary.contains(CORRECT_WORD_1)).thenReturn(false);
        when(stopWordsDictionary.contains(STOP_WORD)).thenReturn(true);

        assertEquals(EXPECTED_2, MetadataGenerator.generate(INPUT_2, basicDictionary, stopWordsDictionary));
    }

    @Test
    public void testGenerateWithNoWrongWords() {
        when(basicDictionary.contains(CORRECT_WORD_2)).thenReturn(true);
        when(basicDictionary.contains(CORRECT_WORD_3)).thenReturn(true);
        when(basicDictionary.contains(CORRECT_WORD_4)).thenReturn(true);
        when(basicDictionary.contains(STOP_WORD)).thenReturn(false);
        when(stopWordsDictionary.contains(CORRECT_WORD_2)).thenReturn(false);
        when(stopWordsDictionary.contains(CORRECT_WORD_3)).thenReturn(false);
        when(stopWordsDictionary.contains(CORRECT_WORD_4)).thenReturn(false);
        when(stopWordsDictionary.contains(STOP_WORD)).thenReturn(true);

        assertEquals(EXPECTED_3, MetadataGenerator.generate(INPUT_3, basicDictionary, stopWordsDictionary));
    }
}
