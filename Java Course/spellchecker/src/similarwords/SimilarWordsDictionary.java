package similarwords;

import dictionaries.Dictionary;

import java.util.*;

public class SimilarWordsDictionary {
    private static Map<String, TreeMap<SimilarityCoefficient, ArrayList<String>>> similarByCoefficientByWord = new HashMap<>(); //MMM STATIC LI???????


    //DALI DA BYDE I TUK SYS STATIC FACTORY?
//    public static SimilarWordsDictionary compose(){
//
//    }

    public static Map<String, TreeMap<SimilarityCoefficient, ArrayList<String>>> getSimilarByCoefficientByWord() {
        return similarByCoefficientByWord;
    }

    public static void add(String word, SimilarityCoefficient simmilarityCoefficient, ArrayList<String> similarWords) {
        if (!similarByCoefficientByWord.containsKey(word)) {
            TreeMap<SimilarityCoefficient, ArrayList<String>> similarWordsMap = new TreeMap<>(); //MAP vmesto tree map?// ideqta e che nqma da se slaga vtori put
            similarWordsMap.put(simmilarityCoefficient, similarWords);                                         // tk na pyrwiq sme obhodili vs v rechnika
            similarByCoefficientByWord.put(word, similarWordsMap);
        }
    }

    public static List<String> getNMostSimilar(String word, int n, Dictionary dictionary) { //da poopravq funkciqta
        if(!similarByCoefficientByWord.containsKey(word)){
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
