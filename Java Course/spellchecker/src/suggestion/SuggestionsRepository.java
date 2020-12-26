package suggestion;

import dictionaries.AbstractDictionary;
import similarwords.SimilarityCoefficient;

import java.util.*;

public class SuggestionsRepository {
    private final AbstractDictionary dictionary;
    private Map<String, TreeMap<SimilarityCoefficient, ArrayList<String>>> suggestionsByCoefficientByWord;

    public SuggestionsRepository(AbstractDictionary dictionary) {
        this.dictionary = dictionary;
        this.suggestionsByCoefficientByWord = new HashMap<>();
    }

    public AbstractDictionary getDictionary(){
        return this.dictionary;
    }

    public Map<String, TreeMap<SimilarityCoefficient, ArrayList<String>>> getSuggestionsByCoefficientByWord() {
        return this.suggestionsByCoefficientByWord;
    }

    public void add(String word, TreeMap<SimilarityCoefficient, ArrayList<String>> suggestionsByCoefficient) {
        if(!this.suggestionsByCoefficientByWord.containsKey(word)){
            this.suggestionsByCoefficientByWord.put(word, suggestionsByCoefficient);
        }
    }

    public List<String> getNSuggestionsForWord(String word, int n) {
        if(!this.suggestionsByCoefficientByWord.containsKey(word)) {
            SuggestionsGenerator.generate(word, this);
        }
        Collection<ArrayList<String>> suggestedWordsInDescendingSimilarity = this.suggestionsByCoefficientByWord.get(word).descendingMap().values();
        List<String> mergedListOfSuggested = new ArrayList<>();
        for (ArrayList<String> nextSuggestedWords : suggestedWordsInDescendingSimilarity) {
            mergedListOfSuggested.addAll(nextSuggestedWords);
        }
        if (mergedListOfSuggested.size() < n) {
            return mergedListOfSuggested;
        }
        return mergedListOfSuggested.subList(0, n);
    }
}
