package dictionaries;

import java.util.Collection;

public abstract class AbstractDictionary {
    protected Collection<String> words;
    protected final DictionaryType type;

    public AbstractDictionary(String words, DictionaryType type) {
        this.type = type;
        this.compose(words);
    }

    protected abstract void compose(String words);

    public Collection<String> getWords() {
        return this.words;
    }

    public boolean contains(String word) {
        return this.words.contains(word);
    }

    protected boolean isShort(String word) {
        return word.length() == 1 || word.length() == 0;
    }
}
