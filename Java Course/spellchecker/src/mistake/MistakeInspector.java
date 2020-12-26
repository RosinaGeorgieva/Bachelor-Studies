package mistake;

import dictionaries.AbstractDictionary;
import util.SentenceDivider;
import util.WordStripper;

import java.util.*;
import java.util.stream.Collectors;

public class MistakeInspector {
    public static Collection<Mistake> inspect(String text, AbstractDictionary dictionary, AbstractDictionary stopWordsDictionary) {
        List<String[]> textLines = text.lines().map(SentenceDivider::divide).collect(Collectors.toList());

        int lineCounter = 1;
        List<Mistake> mistakes = new ArrayList<>();
        for (String[] line : textLines) {
            final int currentLineNumber = lineCounter;
            Arrays.stream(line).distinct().map(WordStripper::strip)
                    .filter(word -> word.length() != 0)
                    .filter(word -> !dictionary.contains(word))
                    .filter(word -> !stopWordsDictionary.contains(word)).collect(Collectors.toSet())
                    .forEach(word -> mistakes.add(new Mistake(currentLineNumber, word)));
            lineCounter++;
        }
        return mistakes;
    }
}
