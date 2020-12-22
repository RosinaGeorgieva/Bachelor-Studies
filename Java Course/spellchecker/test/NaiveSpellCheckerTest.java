import dictionaries.Dictionary;
import dictionaries.MainDictionary;
import dictionaries.StopWordsDictionary;
import metadata.Metadata;
import mistake.Mistake;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import output.SpellcheckOutput;
import similarwords.SimilarWordsDictionary;
import similarwords.SimilarityCoefficient;

import java.io.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NaiveSpellCheckerTest {
    private static final String SAMPLE_DICTIONARY = """
                hello
            %%world
            help
            cat^^
            a
            Amazing
            I
            """;

    private static final String STOP_WORDS_SAMPLE_DICTIONARY = """
            A
            $$you
            you're
            too
            """;

    private static final String SAMPLE_INPUT = "Hallo, you!";
    private static final String WRONG_WORD = "hallo";
    private static final String SUGGESTION_ONE = "help";
    private static final String SUGGESTION_TWO = "hello";
    private static final String STOP_WORD = "you";

    private static final String EXPECTED_OUTPUT = """ 
            Hallo, you!
            = = = Metadata = = =
            10 characters, 1 words, 1 spelling issue(s) found
            = = = Findings = = =
            Line #1, {hallo} - Possible suggestions are {help, hello}
            """;

    private static final int n = 2;
    private static final Metadata EXPECTED_METADATA = new Metadata(10, 1, 1);
    private static final SpellcheckOutput result = new SpellcheckOutput(SAMPLE_INPUT, EXPECTED_METADATA, Set.of(new Mistake(1,
            WRONG_WORD)), Map.of(WRONG_WORD, new TreeMap(Map.of(new SimilarityCoefficient(0.5), new ArrayList(List.of(SUGGESTION_ONE)),
                                                                new SimilarityCoefficient(0.58), new ArrayList(List.of(SUGGESTION_TWO))))));

    @Mock
    private MainDictionary dictionary;

    @Mock
    private StopWordsDictionary stopWordsDictionary;

    @Mock
    private SimilarWordsDictionary similarWordsDictionary;

    @InjectMocks
    private NaiveSpellChecker naiveSpellChecker;

    @Test
    public void testAnalyze() throws IOException {
        Reader inputReader = new StringReader(SAMPLE_INPUT);
        Writer outputWriter = new StringWriter();

        when(dictionary.contains(WRONG_WORD)).thenReturn(false);
        when(dictionary.contains(STOP_WORD)).thenReturn(false);
        when(stopWordsDictionary.contains(WRONG_WORD)).thenReturn(false);
        when(stopWordsDictionary.contains(STOP_WORD)).thenReturn(true);
        when(similarWordsDictionary.getSimilarByCoefficientByWord()).thenReturn(Map.of(WRONG_WORD, new TreeMap(Map.of(new SimilarityCoefficient(0.5), new ArrayList(List.of(SUGGESTION_ONE)),
                new SimilarityCoefficient(0.58), new ArrayList(List.of(SUGGESTION_TWO))))));
        when(similarWordsDictionary.getNMostSimilar(WRONG_WORD, n, dictionary)).thenReturn(List.of(SUGGESTION_ONE, SUGGESTION_TWO));

        naiveSpellChecker.analyze(inputReader, outputWriter, n);
        assertEquals(EXPECTED_OUTPUT, outputWriter.toString());
    }

    @Test
    public void testMetadata() throws IOException {
        Reader inputReader = new StringReader(SAMPLE_INPUT);

        when(dictionary.contains(WRONG_WORD)).thenReturn(false);
        when(dictionary.contains(STOP_WORD)).thenReturn(false);
        when(stopWordsDictionary.contains(WRONG_WORD)).thenReturn(false);
        when(stopWordsDictionary.contains(STOP_WORD)).thenReturn(true);
        when(similarWordsDictionary.getSimilarByCoefficientByWord()).thenReturn(Map.of(WRONG_WORD, new TreeMap(Map.of(new SimilarityCoefficient(0.5), new ArrayList(List.of(SUGGESTION_ONE)),
                                                                                                    new SimilarityCoefficient(0.58), new ArrayList(List.of(SUGGESTION_TWO))))));
        when(similarWordsDictionary.getNMostSimilar(WRONG_WORD, n, dictionary)).thenReturn(List.of(SUGGESTION_ONE, SUGGESTION_TWO));

        assertEquals(EXPECTED_METADATA, naiveSpellChecker.metadata(inputReader));
    }

    @Test
    public void testFindNClosestWords(){
        when(similarWordsDictionary.getNMostSimilar(WRONG_WORD, n, dictionary)).thenReturn(List.of(SUGGESTION_ONE, SUGGESTION_TWO));

        assertEquals(List.of(SUGGESTION_ONE, SUGGESTION_TWO), naiveSpellChecker.findClosestWords(WRONG_WORD, n));
    }
}
