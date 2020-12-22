package similarwords;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Double product = Double.valueOf(0);
        for (String twoGram : firstVector.keySet()) {
            if (secondVector.containsKey(twoGram)) {
                product += firstVector.get(twoGram) * secondVector.get(twoGram);
            }
        }
        return product;
    }

    private static Double vectorLength(Map<String, Integer> vector) {
        Double length = Double.valueOf(0);
        for (String twoGram : vector.keySet()) {
            length += Math.pow(vector.get(twoGram), 2);
        }
        return Math.sqrt(length);
    }

    private static Map<String, Integer> vectorRepresentation(String word) {
        Map<String, Integer> vector = new HashMap<>(); //imeto na vector e tupo
        List<String> twoGrams = getTwoGrams(word);
        for (String twoGram : twoGrams) {
            if (vector.containsKey(twoGram)) {
                vector.put(twoGram, vector.get(twoGram) + 1);
            } else {
                vector.put(twoGram, 1);
            }
        }
        return vector;
    }

    private static List<String> getTwoGrams(String word) {
        List<String> twoGrams = new ArrayList<>();
        for (int i = 0; i < word.length() - 1; i++) { //vj dokude ti se wyrti cikyla
            twoGrams.add(word.substring(i, i + 2));
        }
        return twoGrams;
    }
}
