package dictionaries;

import util.WordStripper;

import java.util.stream.Collectors;

public class BasicDictionary extends AbstractDictionary {
    public BasicDictionary(String words, DictionaryType type) {
        super(words, type);
    }

    @Override
    protected void compose(String words) {
        this.words = words.lines().map(WordStripper::strip).filter(word -> !isShort(word)).collect(Collectors.toSet());
    }
}
