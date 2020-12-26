package spellchecker;

import dictionaries.AbstractDictionary;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import suggestion.SuggestionsRepository;
import similarwords.SimilarityCoefficient;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SuggestionsRepositoryTest {
    private SuggestionsRepository suggestionsRepository;

    @Mock
    private AbstractDictionary dictionary;

    @Before
    public void setUp() {
        suggestionsRepository = new SuggestionsRepository(dictionary);
    }

    private static List<String> DICTIONARY_WORDS = List.of("help", "hallo", "hilfe", "cat", "dog", "puppy", "love");

    private static final String INPUT_1 = "hello";
    private static final int N_1 = 0;
    private static final List<String> EXPECTED_1 = new ArrayList<>();

    private static final String INPUT_2 = INPUT_1;
    private static final int N_2 = 5;
    private static final List<String> EXPECTED_2 = new ArrayList<>(List.of("help", "hallo", "love", "hilfe", "cat"));

    private static final String INPUT_3 = "hello";
    private static final int N_3 = 1;
    private static final SimilarityCoefficient SIMILARITY_COEFFICIENT_3 = new SimilarityCoefficient(Double.valueOf(0.58));
    private static final ArrayList<String> SUGGESTED_3 = new ArrayList<>(List.of("help"));
    private static TreeMap<SimilarityCoefficient, ArrayList<String>> SUGGESTIONS_BY_SIMILARITY_BY_WORD = new TreeMap<>(Map.of(SIMILARITY_COEFFICIENT_3, SUGGESTED_3));
    private static final List<String> EXPECTED_3 = new ArrayList<>(List.of("help"));

    private static final String INPUT_4 = "hallo";
    private static final int N_4 = 5;
    private static final List<String> EXPECTED_4 = new ArrayList<>(List.of("hallo", "love", "help", "hilfe", "cat"));

    @Test
    public void testGetNSuggestionsWithZero() {
        when(dictionary.getWords()).thenReturn(new ArrayList<>());

        assertEquals(EXPECTED_1, suggestionsRepository.getNSuggestionsForWord(INPUT_1, N_1));
    }

    @Test
    public void testGetNSuggestionsForNewWord() {
        when(dictionary.getWords()).thenReturn(DICTIONARY_WORDS);

        assertEquals(EXPECTED_2, suggestionsRepository.getNSuggestionsForWord(INPUT_2, N_2));
    }

    @Test
    public void testGetNSuggestionsForExistingWord() {
        when(dictionary.getWords()).thenReturn(DICTIONARY_WORDS);

        suggestionsRepository.add(INPUT_3, SUGGESTIONS_BY_SIMILARITY_BY_WORD);
        assertEquals(EXPECTED_3, suggestionsRepository.getNSuggestionsForWord(INPUT_3, N_3));
    }

    @Test
    public void testGetNSuggestionsForWordFromDictionary() {
        when(dictionary.getWords()).thenReturn(DICTIONARY_WORDS);

        assertEquals(EXPECTED_4, suggestionsRepository.getNSuggestionsForWord(INPUT_4, N_4));
    }
}
