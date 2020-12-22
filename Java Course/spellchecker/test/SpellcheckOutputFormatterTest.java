import dictionaries.Dictionary;
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

//da probvam za poveche ot 1 greshka kak izglejda
//dobavqne na test za text bez greshki!!!!!!!!!!!!!!!!!!!; s n poveche otkolkoto ima!
//NIKUDE NE SUM ZATVORILA READER-ITE!!!; ///dobavqne na test za poveche ot 1 sgreshena ! ; da vidq dali trqbwa da se pokazwa s orig zapis sled line#... v {}-te
@RunWith(MockitoJUnitRunner.class)
public class SpellcheckOutputFormatterTest { //SUPER OMAZAN KLAS
    private static final String EXPECTED_1 = """ 
            Hello, you!
            = = = Metadata = = =
            12 characters, 1 words, 0 spelling issue(s) found
            = = = Findings = = =
            """;
    private static final String CORRECT_INPUT = "Hello, you!";
    private static final String EXPECTED_2 = """ 
            Hallo, you!
            = = = Metadata = = =
            12 characters, 1 words, 1 spelling issue(s) found
            = = = Findings = = =
            Line #1, {hallo} - Possible suggestions are {hello}
            """;
    private static final String MISTAKEN_INPUT = "Hallo, you!";
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
    private static ArrayList<String> suggestions;
    private static TreeMap<SimilarityCoefficient, ArrayList<String>> suggestionsBySimilarity;
    private static Map<String, TreeMap<SimilarityCoefficient, ArrayList<String>>> suggestionsBySimilarityByWord;
    private static Reader inputReader;
    private static Writer outputWriter;

    @Mock
    private Dictionary mainDictionary;

    @Mock
    private Dictionary stopWordsDictionary;

    @Mock
    private SimilarWordsDictionary similarWordsDictionary;

    @BeforeClass // ne biva da se syzdava reader tuka
    public static void setUp(){
        metadata = new Metadata(NUMBER_OF_CHARACTERS, NUMBER_OF_WORDS, NUMBER_OF_MISTAKES);
        expectedMistake= new Mistake(MISTAKE_LINE, MISTAKEN_WORD);
        mistakes = new HashSet<>();
        mistakes.add(expectedMistake);
        similarityCoefficient = new SimilarityCoefficient(SIMILARITY_COEFFICIENT);
        suggestions = new ArrayList<>();
        suggestions.add(CORRECT_WORD);
        suggestionsBySimilarity = new TreeMap<>();
        suggestionsBySimilarity.put(similarityCoefficient, suggestions);
        suggestionsBySimilarityByWord = new HashMap<>();
        suggestionsBySimilarityByWord.put(MISTAKEN_WORD, suggestionsBySimilarity);
    }

    @Test
    public void testGenerateNWithMistaken(){
        inputReader = new StringReader(MISTAKEN_INPUT);
        spellcheckOutput = new SpellcheckOutput(MISTAKEN_INPUT, metadata, mistakes, suggestionsBySimilarityByWord);
        outputWriter = new StringWriter();

        when(mainDictionary.contains(MISTAKEN_WORD)).thenReturn(false);
        when(mainDictionary.contains(STOP_WORD)).thenReturn(false);
        when(stopWordsDictionary.contains(MISTAKEN_WORD)).thenReturn(false);
        when(stopWordsDictionary.contains(STOP_WORD)).thenReturn(true);
        when(similarWordsDictionary.getNMostSimilar(MISTAKEN_WORD, n, mainDictionary)).thenReturn(suggestions);

        SpellcheckOutputFormatter.generate(spellcheckOutput, n, outputWriter, mainDictionary, similarWordsDictionary);
        String actual = outputWriter.toString();
        assertEquals(EXPECTED_2, actual);
    }

    @Test
    public void testGenerateNWithCorrect(){
        inputReader = new StringReader(CORRECT_INPUT);
        spellcheckOutput = new SpellcheckOutput(CORRECT_INPUT, new Metadata(NUMBER_OF_CHARACTERS, NUMBER_OF_WORDS, 0), new HashSet<Mistake>(), suggestionsBySimilarityByWord);
        outputWriter = new StringWriter();

        when(mainDictionary.contains(CORRECT_WORD)).thenReturn(true);
        when(mainDictionary.contains(STOP_WORD)).thenReturn(false);
        when(stopWordsDictionary.contains(CORRECT_WORD)).thenReturn(false);
        when(stopWordsDictionary.contains(STOP_WORD)).thenReturn(true);
        when(similarWordsDictionary.getNMostSimilar(CORRECT_WORD, n, mainDictionary)).thenReturn(suggestions);

        SpellcheckOutputFormatter.generate(spellcheckOutput, n, outputWriter, mainDictionary, similarWordsDictionary);
        String actual = outputWriter.toString();
        assertEquals(EXPECTED_1, actual);
    }
}
