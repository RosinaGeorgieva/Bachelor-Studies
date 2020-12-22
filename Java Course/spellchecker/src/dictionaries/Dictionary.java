package dictionaries;

import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

public abstract class Dictionary {
    protected Set<String> words;
    protected final DictionaryType type;

    public Dictionary(Reader text, DictionaryType type){
        this.type = type;
        this.words = new HashSet<>();
        this.compose(text);
    }

    protected abstract void compose(Reader text);

    public Set<String> getWords(){
        return this.words;
    }

    public boolean contains(String word){
        return this.words.contains(word);
    }

    protected static boolean isShort(String word){
        return word.length() == 1;
    }
}
