package spellchecker;

import dictionaries.BasicDictionary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import suggestion.SuggestionsGenerator;
import similarwords.SimilarityCoefficient;
import suggestion.SuggestionsRepository;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SuggestionsGeneratorTest { //oshte testove est; TESTOWE ZA PRI NESGRESHENA KAKWO SHTE VYRNE
    private static final String MISTAKEN_WORD = "hello";
    private static final String FIRST_SUGGESTION = "hallo";
    private static final String SECOND_SUGGESTION = "help";

    private static final int N = 2;

    private static final SimilarityCoefficient FIRST_SUGGESTION_SIMILARITY = new SimilarityCoefficient(Double.valueOf(0.5));
    private static final SimilarityCoefficient SECOND_SUGGESTION_SIMILARITY = new SimilarityCoefficient(Double.valueOf(0.58));

    private static final ArrayList<String> EXPECTED_SUGGESTIONS = new ArrayList<>(List.of(SECOND_SUGGESTION, FIRST_SUGGESTION));
    private static final TreeMap<SimilarityCoefficient, ArrayList<String>> SUGGESTIONS_BY_SIMILARITY = new TreeMap(Map.of(FIRST_SUGGESTION_SIMILARITY, new ArrayList(List.of(FIRST_SUGGESTION)),
            SECOND_SUGGESTION_SIMILARITY, new ArrayList(List.of(SECOND_SUGGESTION))));
    @Mock
    private BasicDictionary basicDictionary;

    @Mock
    private SuggestionsRepository suggestionsRepository;

    @Test
    public void testGenerate() {
        when(suggestionsRepository.getDictionary()).thenReturn(basicDictionary);
        when(basicDictionary.getWords()).thenReturn(Set.of(FIRST_SUGGESTION, SECOND_SUGGESTION));

        SuggestionsGenerator.generate(MISTAKEN_WORD, suggestionsRepository);
        assertEquals(SUGGESTIONS_BY_SIMILARITY, SuggestionsGenerator.generate(MISTAKEN_WORD, suggestionsRepository));
    }

//    private static final String INPUT_1 = "hallo";
//    private
//    private static final List<String>
//
//    @Test
//    public void testGenerateZeroSuggestions() {
//
//    }
}
