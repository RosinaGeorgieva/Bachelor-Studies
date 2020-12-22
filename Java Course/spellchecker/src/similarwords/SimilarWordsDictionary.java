package similarwords;

import dictionaries.Dictionary;

import java.util.*;

public class SimilarWordsDictionary {
    private Map<String, TreeMap<SimilarityCoefficient, ArrayList<String>>> similarByCoefficientByWord; //MMM STATIC LI???????

    public SimilarWordsDictionary() {
        similarByCoefficientByWord = new HashMap<>();
    }

    public Map<String, TreeMap<SimilarityCoefficient, ArrayList<String>>> getSimilarByCoefficientByWord() {
        return similarByCoefficientByWord;
    }

    public void add(String word, SimilarityCoefficient simmilarityCoefficient, ArrayList<String> similarWordsWithSuchCoeff) {
        if (!similarByCoefficientByWord.containsKey(word)) {
            TreeMap<SimilarityCoefficient, ArrayList<String>> similarWordsMap = new TreeMap<>(); //MAP vmesto tree map?// ideqta e che nqma da se slaga vtori put
            similarWordsMap.put(simmilarityCoefficient, similarWordsWithSuchCoeff);                                         // tk na pyrwiq sme obhodili vs v rechnika
            similarByCoefficientByWord.put(word, similarWordsMap);
        } else {//wtf temp???
            TreeMap<SimilarityCoefficient, ArrayList<String>> temp = similarByCoefficientByWord.get(word);
            if(!temp.containsKey(simmilarityCoefficient)){
                temp.put(simmilarityCoefficient, similarWordsWithSuchCoeff);
            } else {
                for(String simmilar : similarWordsWithSuchCoeff){
                    if(!temp.get(simmilarityCoefficient).contains(simmilar)) {
                        temp.get(simmilarityCoefficient).add(simmilar);
                    }
                }
            }
        }
    }

    public List<String> getNMostSimilar(String word, int n, Dictionary dictionary) { //da poopravq funkciqta
        if (!similarByCoefficientByWord.containsKey(word)) {
            similarByCoefficientByWord.put(word, SimilarWordsGenerator.generate(word, dictionary));
        }
        Collection<ArrayList<String>> suggestedWords = similarByCoefficientByWord.get(word).values();
        List<String> mergedListOfSuggested = new ArrayList<>();
        for (ArrayList<String> nextSuggestedWords : suggestedWords) {
            mergedListOfSuggested.addAll(nextSuggestedWords);
        }
        if (mergedListOfSuggested.size() < n) {
            return mergedListOfSuggested;
        }
        return mergedListOfSuggested.subList(0, n);
    }
}
