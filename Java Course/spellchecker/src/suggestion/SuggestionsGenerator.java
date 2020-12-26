package suggestion;

import similarwords.SimilarityCoefficient;
import similarwords.SimilarityCoefficientCalculator;

import java.util.*;

public class SuggestionsGenerator {
    public static Map<SimilarityCoefficient, ArrayList<String>> generate(String word, SuggestionsRepository suggestionsRepository) { //da q poopvawq tazi funkciq
        TreeMap<SimilarityCoefficient, ArrayList<String>> wordsBySimilarityCoefficient = new TreeMap<>();
        Collection<String> wordsFromDictionary = suggestionsRepository.getDictionary().getWords();

        for (String wordFromDictionary : wordsFromDictionary) {
            SimilarityCoefficient similarityCoefficient = SimilarityCoefficientCalculator.calculate(word, wordFromDictionary);
            if (wordsBySimilarityCoefficient.containsKey(similarityCoefficient)) {
                wordsBySimilarityCoefficient.get(similarityCoefficient).add(wordFromDictionary);
            } else {
                ArrayList<String> similarWords = new ArrayList<>();
                similarWords.add(wordFromDictionary);
                wordsBySimilarityCoefficient.put(similarityCoefficient, similarWords);
            }
        }
        suggestionsRepository.add(word, wordsBySimilarityCoefficient);
        return wordsBySimilarityCoefficient;
    }
}
