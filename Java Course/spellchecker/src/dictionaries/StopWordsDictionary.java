package dictionaries;

import util.WordStripper;

import java.util.stream.Collectors;

public class StopWordsDictionary extends AbstractDictionary {
    public StopWordsDictionary(String words, DictionaryType type) {
        super(words, type);
    }

    @Override
    protected void compose(String words) {
        this.words = words.lines().map(WordStripper::strip).collect(Collectors.toSet());
    }
}
