import dictionaries.Dictionary;
import dictionaries.DictionaryType;
import dictionaries.MainDictionary;
import dictionaries.StopWordsDictionary;
import metadata.Metadata;
import mistake.Mistake;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import output.SpellcheckOutput;
import output.SpellcheckOutputFormatter;
import similarwords.SimilarWordsDictionary;
import similarwords.SimilarityCoefficient;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

//NIKUDE NE SUM ZATVORILA READER-ITE!!!; ///dobavqne na test za poveche ot 1 sgreshena ! ; da vidq dali trqbwa da se pokazwa s orig zapis sled line#... v {}-te
@RunWith(MockitoJUnitRunner.class)
public class SpellcheckOutputFormatterTest { //dobavqne na test za text bez greshki!!!!!!!!!!!!!!!!!!!; s n poveche otkolkoto ima!
    private static final String EXPECTED = """ 
            Hallo, world!
            = = = Metadata = = =
            12 characters, 1 words, 1 spelling issue(s) found
            = = = Findings = = =
            Line #1, {hallo} - Possible suggestions are {hello}
            """;
    private static final String INPUT = "Hallo, you!";
    private static final int n = 1;
    private static final String MISTAKEN_WORD = "hallo";
    private static final String CORRECT_WORD = "hello";
    private static final String STOP_WORD = "you";
    private static final int MISTAKE_LINE = 1;
    private static final int NUMBER_OF_CHARACTERS = 12;
    private static final int NUMBER_OF_WORDS = 1;
    private static final int NUMBER_OF_MISTAKES = 1;
    private static final double SIMILARITY_COEFFICIENT = 0.5;
    private static Metadata metadata;
    private static Mistake expectedMistake;
    private static Set<Mistake> mistakes;
    private static SpellcheckOutput spellcheckOutput;
    private static SimilarityCoefficient similarityCoefficient;
    private static TreeMap<SimilarityCoefficient, ArrayList<String>> suggestionsBySimilarity;
    private static Map<String, TreeMap<SimilarityCoefficient, ArrayList<String>>> suggestionsBySimilarityByWord;

    private static Dictionary mainDictionary;
    private static Dictionary stopWordsDictionary;
    private static Reader inputReader;
    private static Writer outputWriter;

    @Mock
    private SimilarWordsDictionary similarWordsDictionary;

    @BeforeClass
    public static void setUp(){
        metadata = new Metadata(NUMBER_OF_CHARACTERS, NUMBER_OF_WORDS, NUMBER_OF_MISTAKES);
        expectedMistake= new Mistake(MISTAKE_LINE, MISTAKEN_WORD);
        mistakes = new HashSet<>();
        mistakes.add(expectedMistake);
        similarityCoefficient = new SimilarityCoefficient(SIMILARITY_COEFFICIENT);
        ArrayList<String> suggestions = new ArrayList<>();
        suggestions.add(CORRECT_WORD);
        suggestionsBySimilarity = new TreeMap<>();
        suggestionsBySimilarity.put(similarityCoefficient, suggestions);
        suggestionsBySimilarityByWord = new HashMap<>();
        suggestionsBySimilarityByWord.put(MISTAKEN_WORD, suggestionsBySimilarity);
    }

    @Test
    public void testGenerateNWithMistaken(){
        inputReader = new StringReader(INPUT);
        mainDictionary = new MainDictionary(inputReader, DictionaryType.MAIN_DICTIONARY);
        stopWordsDictionary = new StopWordsDictionary(inputReader, DictionaryType.STOP_WORDS_DICTIONARY);
        spellcheckOutput = new SpellcheckOutput(inputReader, metadata, mistakes, suggestionsBySimilarityByWord);
        outputWriter = new StringWriter();

        when(similarWordsDictionary.getNMostSimilar(MISTAKEN_WORD, n, mainDictionary)).thenReturn(suggestionsBySimilarity.get(SIMILARITY_COEFFICIENT));

        SpellcheckOutputFormatter.generate(spellcheckOutput, n, outputWriter);
        String actual = outputWriter.toString();
        assertEquals(EXPECTED, actual);
    }
}
