import dictionaries.MainDictionary;
import dictionaries.StopWordsDictionary;
import metadata.Metadata;
import metadata.MetadataGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import wordcleanser.WordNormalizer;

import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MetadataGeneratorTest { //oshte testowe
    private static final String MISTAKEN_WORD = "Hallo";
//    private static final String CASE_INSENSITIVE_MISTAKEN_WORD = "hello";
    private static final String STOP_WORD = "you";
    private static final String INPUT = MISTAKEN_WORD + " " + STOP_WORD;
    private static final int NUMBER_OF_CHARACTERS = 8;
    private static final int NUMBER_OF_WORDS = 1;
    private static final int NUMBER_OF_MISTAKES = 1;
    private static final Metadata EXPECTED = new Metadata(NUMBER_OF_CHARACTERS, NUMBER_OF_WORDS, NUMBER_OF_MISTAKES);

    @Mock
    private MainDictionary dictionary;

    @Mock
    private StopWordsDictionary stopWordsDictionary;

    @Mock
    private WordNormalizer wordNormalizer;

    @Test
    public void testGenerate(){ //pass
        Reader inputReader = new StringReader(INPUT);

        when(dictionary.contains(MISTAKEN_WORD)).thenReturn(true);
        when(dictionary.contains(STOP_WORD)).thenReturn(false);
        when(stopWordsDictionary.contains(MISTAKEN_WORD)).thenReturn(false);
        when(stopWordsDictionary.contains(STOP_WORD)).thenReturn(true); //kak da mock-na word normalizer-a???
//        when(WordNormalizer.makeCaseInsensitive(MISTAKEN_WORD)).thenReturn(CASE_INSENSITIVE_MISTAKEN_WORD);
//        when(WordNormalizer.makeCaseInsensitive(STOP_WORD)).thenReturn(STOP_WORD); // DA DOBAVQ ZA REMOVE ALL WHITESPACES!!!
//        when(WordNormalizer.removeBorderNonAlphanumeric(MISTAKEN_WORD)).thenReturn(CASE_INSENSITIVE_MISTAKEN_WORD);
//        when(WordNormalizer.removeBorderNonAlphanumeric(STOP_WORD)).thenReturn(STOP_WORD);

        assertEquals(EXPECTED, MetadataGenerator.generate(inputReader, dictionary, stopWordsDictionary));
    }
}
