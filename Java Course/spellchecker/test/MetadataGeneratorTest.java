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
    private static final String MISTAKEN_WORD = "hallo";
//    private static final String CASE_INSENSITIVE_MISTAKEN_WORD = "hello";
    private static final String STOP_WORD = "you";
    private static final String INPUT = "Hallo, you!";
    private static final int NUMBER_OF_CHARACTERS = 10;
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

        when(dictionary.contains(MISTAKEN_WORD)).thenReturn(false);
        when(dictionary.contains(STOP_WORD)).thenReturn(false);
        when(stopWordsDictionary.contains(MISTAKEN_WORD)).thenReturn(false);
        when(stopWordsDictionary.contains(STOP_WORD)).thenReturn(true);

        assertEquals(EXPECTED, MetadataGenerator.generate(INPUT, dictionary, stopWordsDictionary));
    }
}
