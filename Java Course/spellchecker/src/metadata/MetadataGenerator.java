package metadata;

import dictionaries.Dictionary;
import wordcleanser.WordNormalizer;

import java.util.Set;
import java.util.stream.Collectors;

public class MetadataGenerator { // ne e minal prez stream api refactoring
    public static Metadata generate(String text, Dictionary dictionary, Dictionary stopWordsDictionary) {

        int characterCount = 0;
        int wordCount = 0;
        int mistakesCount = 0;

        Set<String[]> lines = text.lines().map(WordNormalizer::removeAllWhitespaces).collect(Collectors.toSet());

        for (String[] line : lines) {
            for (String word : line) {
                word = WordNormalizer.makeCaseInsensitive(word);

                characterCount += countCharacters(word);

                word = WordNormalizer.removeBorderNonAlphanumeric(word);

                wordCount += countWord(word, stopWordsDictionary);
                mistakesCount += countMistakes(word, dictionary, stopWordsDictionary);
            }
        }

        return new Metadata(characterCount, wordCount, mistakesCount);
    }

    private static int countCharacters(String word) {
        return word.length();
    }

    private static int countWord(String word, Dictionary stopWordsDictionary) {
        if (!stopWordsDictionary.contains(word)) {
            return 1;
        }
        return 0;
    }

    private static int countMistakes(String word, Dictionary dictionary, Dictionary stopWordsDictionary) {
        if (!dictionary.contains(word) && !stopWordsDictionary.contains(word)) {
            return 1;
        }
        return 0;
    }
}
