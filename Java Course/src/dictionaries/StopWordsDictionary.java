package dictionaries;

import util.WordCleanser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

public class StopWordsDictionary  {
    protected Set<String> words;

    private StopWordsDictionary(Set<String> words){
        this.words = words;
    }

    public static StopWordsDictionary compose(Reader text) {
        Set<String> words = new HashSet<>();
        try(var bufferedReader = new BufferedReader(text)){
            String wordPerLine;

            while((wordPerLine = bufferedReader.readLine()) != null){
                wordPerLine = WordCleanser.makeCaseInsensitive(wordPerLine);
                wordPerLine = WordCleanser.removeBorderWhitespaces(wordPerLine);
                wordPerLine = WordCleanser.removeBorderNonAlphanumeric(wordPerLine);

                words.add(wordPerLine);
            }
            return new StopWordsDictionary(words);

        } catch (IOException exception){
            return null;
        }
    }
    public Set<String> getWords(){
        return this.words;
    }

    public boolean contains(String word){
        return this.words.contains(word);
    }

    private static boolean isShort(String word){
        return word.length() == 1;
    }
}
