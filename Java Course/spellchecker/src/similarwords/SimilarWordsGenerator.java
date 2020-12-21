package similarwords;

import dictionaries.Dictionary;

import java.util.*;

public class SimilarWordsGenerator {
    public static TreeMap<SimilarityCoefficient, ArrayList<String>> generate(String word, Dictionary dictionary){ //da q poopvawq tazi funkciq
        Collection<String> wordsFromDictionary = dictionary.getWords();
        TreeMap<SimilarityCoefficient, ArrayList<String>> wordsBySimilarityCoefficient = new TreeMap<>();
        for(String wordFromDictionary : wordsFromDictionary){
            SimilarityCoefficient similarityCoefficient = SimilarityCoefficientCalculator.calculate(word, wordFromDictionary);
            if(wordsBySimilarityCoefficient.containsKey(similarityCoefficient)){
                wordsBySimilarityCoefficient.get(similarityCoefficient).add(wordFromDictionary);
            } else {
                ArrayList<String> similarWords = new ArrayList<>();
                similarWords.add(wordFromDictionary);
                wordsBySimilarityCoefficient.put(similarityCoefficient, similarWords);
            }
        }

        return wordsBySimilarityCoefficient;
    }
}
