package dictionaries;

import wordcleanser.WordNormalizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

public abstract class Dictionary {
    protected Set<String> words; //FINAL??
    protected DictionaryType type;

    public Dictionary(Reader text, DictionaryType type){
        this.type = type;
        this.words = new HashSet<>();
        this.compose(text);
    }

    protected void compose(Reader text) {
        try(var bufferedReader = new BufferedReader(text)){
            String wordPerLine;

            while((wordPerLine = bufferedReader.readLine()) != null){
                wordPerLine = WordNormalizer.normalize(wordPerLine);

                if(!isShort(wordPerLine) && this.type == DictionaryType.MAIN_DICTIONARY) {
                    words.add(wordPerLine);
                }
            }

        } catch (IOException exception){

            //...
        }
    }

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
