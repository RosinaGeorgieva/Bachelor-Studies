import com.sun.source.tree.Tree;
import dictionaries.MainDictionary;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import similarwords.SimilarWordsDictionary;
import similarwords.SimilarityCoefficient;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SimilarWordsDictionaryTest {//ne e iztestvan add; oshte testove za null duma;
    private static final String WORD = "hello";
    private static final String FIRST_SIMILAR = "hallo";
    private static final SimilarityCoefficient firstSimilarity = new SimilarityCoefficient(Double.valueOf(0.5));
    private static final String SECOND_SIMILAR = "help";
    private static final SimilarityCoefficient secondSimilarity = new SimilarityCoefficient(Double.valueOf(0.57));
    private static final int n = 2;
    private static TreeMap<SimilarityCoefficient, ArrayList<String>> suggestionsBySimilarity;
    private static Map<String, TreeMap<SimilarityCoefficient, ArrayList<String>>> suggestionsBySimilarityByWord;
    private static List<String> expectedSuggestions = new ArrayList<>(List.of(FIRST_SIMILAR, SECOND_SIMILAR));

    private static SimilarWordsDictionary similarWordsDictionary;

    @Mock
    private MainDictionary dictionary; //dali trqbwa da e mock-ing tuk??

    @BeforeClass
    public static void setUp(){
        similarWordsDictionary = new SimilarWordsDictionary();
        suggestionsBySimilarity = new TreeMap<>();
        suggestionsBySimilarity.put(firstSimilarity, new ArrayList<>(List.of(FIRST_SIMILAR)));
        suggestionsBySimilarity.put(secondSimilarity, new ArrayList<>(List.of(SECOND_SIMILAR)));
        suggestionsBySimilarityByWord = new HashMap<>();
        suggestionsBySimilarityByWord.put(WORD, suggestionsBySimilarity);
    }

    @Test//da dobavq test za poveche ot 1 element
    public void testAdd(){ //da go vidq adekvaten li e
        similarWordsDictionary.add(WORD, firstSimilarity, new ArrayList<>(List.of(FIRST_SIMILAR)));
        assertEquals(new ArrayList<>(List.of(FIRST_SIMILAR)), similarWordsDictionary.getSimilarByCoefficientByWord().get(WORD).get(firstSimilarity));
    }

    @Test
    public void testGetNMostSimilar(){
        similarWordsDictionary.add(WORD, firstSimilarity, new ArrayList<>(List.of(FIRST_SIMILAR)));
        similarWordsDictionary.add(WORD, secondSimilarity, new ArrayList<>(List.of(SECOND_SIMILAR)));
        when(dictionary.getWords()).thenReturn(Set.of(FIRST_SIMILAR, SECOND_SIMILAR));
        System.out.println(similarWordsDictionary.getSimilarByCoefficientByWord());
        assertEquals(expectedSuggestions, similarWordsDictionary.getNMostSimilar(WORD, n, dictionary));
    }
}
