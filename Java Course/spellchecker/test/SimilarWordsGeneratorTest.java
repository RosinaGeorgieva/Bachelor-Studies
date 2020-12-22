import dictionaries.Dictionary;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import similarwords.SimilarWordsGenerator;
import similarwords.SimilarityCoefficient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SimilarWordsGeneratorTest { //oshte testove est
    private static final String WORD = "hello";
    private static final String FIRST_SIMILAR = "hallo";
    private static SimilarityCoefficient firstCoefficient;
    private static final String SECOND_SIMILAR = "help";
    private static SimilarityCoefficient secondCoefficient;
    private static TreeMap<SimilarityCoefficient, ArrayList<String>> expectedSuggestionsBySimilarity;

    @Mock
    private Dictionary dictionary;

    @Before
    public void setUp(){
        firstCoefficient = new SimilarityCoefficient(0.5);
        secondCoefficient = new SimilarityCoefficient(0.58); //da se iznesat
        expectedSuggestionsBySimilarity = new TreeMap<>();
        expectedSuggestionsBySimilarity.put(firstCoefficient, new ArrayList<>(List.of(FIRST_SIMILAR)));
        expectedSuggestionsBySimilarity.put(secondCoefficient, new ArrayList<>(List.of(SECOND_SIMILAR)));
    }

    @Test
    public void testGenerate(){
        when(dictionary.getWords()).thenReturn(Set.of(FIRST_SIMILAR, SECOND_SIMILAR));
        assertEquals(expectedSuggestionsBySimilarity, SimilarWordsGenerator.generate(WORD, dictionary));
    }
}
