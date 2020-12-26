package spellchecker;

import dictionaries.AbstractDictionary;
import util.SentenceDivider;
import util.WordStripper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MetadataGenerator {
    public static Metadata generate(String text, AbstractDictionary dictionary, AbstractDictionary stopWordsDictionary) {

        int characterCount = 0;
        int wordCount = 0;
        int mistakesCount = 0;

        List<String[]> textLines = text.lines().map(SentenceDivider::divide).collect(Collectors.toList());
        for (String[] line : textLines) {
            List<String> allWordsInLine = Arrays.stream(line).distinct().map(WordStripper::makeCaseInsensitive).collect(Collectors.toList());
            characterCount += allWordsInLine.stream().mapToInt(word -> word.length()).sum();
            allWordsInLine = allWordsInLine.stream().map(WordStripper::removeBorderNonAlphanumeric).collect(Collectors.toList());
            wordCount += allWordsInLine.stream().mapToInt(word -> !stopWordsDictionary.contains(word) ? 1 : 0).sum();
            mistakesCount += allWordsInLine.stream().mapToInt(word -> (!dictionary.contains(word) && !stopWordsDictionary.contains(word)) ? 1 : 0).sum();
        }

        return new Metadata(characterCount, wordCount, mistakesCount);
    }
}
