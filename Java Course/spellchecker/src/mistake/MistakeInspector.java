package mistake;

import dictionaries.Dictionary;
import wordcleanser.WordNormalizer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

//kak moga da go napravq izcqlo sus stream api
public class MistakeInspector {//da mahna hardcode-natitie neshta
    public static Set<Mistake> inspect(String text, Dictionary dictionary, Dictionary stopWordsDictionary) {
        Set<String[]> lines = text.lines().map(WordNormalizer::removeAllWhitespaces).collect(Collectors.toSet());
        Set<Mistake> mistakes = new HashSet<>();
        int lineNumber = 1;
        for (String[] line : lines) {
            Set<String> words = Arrays.stream(line).distinct().map(WordNormalizer::normalize)
                    .filter(word -> !word.equals(""))
                    .filter(word -> !dictionary.contains(word))
                    .filter(word -> !stopWordsDictionary.contains(word)).collect(Collectors.toSet());
            for (String word : words) {
                mistakes.add(new Mistake(lineNumber, word));
            }
            lineNumber++;
        }
        return mistakes;
    }
}
