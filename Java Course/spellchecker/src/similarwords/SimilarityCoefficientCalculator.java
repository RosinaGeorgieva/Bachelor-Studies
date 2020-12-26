package similarwords;

import java.util.*;

public class SimilarityCoefficientCalculator {
    public static SimilarityCoefficient calculate(String firstWord, String secondWord) {
        return new SimilarityCoefficient(Math.round(cosSimilarity(firstWord, secondWord) * 100.0) / 100.0);
    }

    private static Double cosSimilarity(String firstWord, String secondWord) {
        Map<String, Integer> firstVector = vectorRepresentation(firstWord);
        Map<String, Integer> secondVector = vectorRepresentation(secondWord);
        Double firstVectorLength = vectorLength(firstVector);
        Double secondVectorLength = vectorLength(secondVector);
        return vectorProduct(firstVector, secondVector) / (firstVectorLength * secondVectorLength);
    }

    private static Double vectorProduct(Map<String, Integer> firstVector, Map<String, Integer> secondVector) {
        return firstVector.keySet().stream().filter(key -> secondVector.containsKey(key))
                .mapToDouble(key -> firstVector.get(key) * secondVector.get(key))
                .sum();
    }

    private static Double vectorLength(Map<String, Integer> vector) {
        return Math.sqrt(vector.values().stream().mapToDouble(occurrence -> Math.pow(occurrence, 2)).sum());
    }

    private static Map<String, Integer> vectorRepresentation(String word) {
        Map<String, Integer> twoGramsVector = new HashMap<>();
        getTwoGrams(word).stream().filter(twoGram -> twoGramsVector.containsKey(twoGram)).forEach(twoGram -> twoGramsVector.put(twoGram, twoGramsVector.get(twoGram) + 1));
        getTwoGrams(word).stream().filter(twoGram -> !twoGramsVector.containsKey(twoGram)).forEach(twoGram -> twoGramsVector.put(twoGram, 1));
        return twoGramsVector;
    }

    private static Collection<String> getTwoGrams(String word) {
        List<String> twoGrams = new ArrayList<>();
        for (int i = 0; i < word.length() - 1; i++) {
            twoGrams.add(word.substring(i, i + 2));
        }
        return twoGrams;
    }
}
