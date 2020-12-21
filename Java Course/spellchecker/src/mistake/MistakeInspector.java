package mistake;

import dictionaries.Dictionary;
import wordcleanser.WordNormalizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

public class MistakeInspector {//eventualno da sloja hashmap za textove, koito veche sa inspectirani
    public static Set<Mistake> inspect(Reader text, Dictionary dictionary, Dictionary stopWordsDictionary) {
        try (var bufferedReader = new BufferedReader(text)) {
            Set<Mistake> mistakes = new HashSet<>();
            String line;
            int lineNumber = 1;
            while ((line = bufferedReader.readLine()) != null) {
                String[] words = WordNormalizer.removeAllWhitespaces(line);
                for (String word : words) {
                    word = WordNormalizer.makeCaseInsensitive(word);
                    word = WordNormalizer.removeBorderNonAlphanumeric(word);

                    if (!dictionary.contains(word) && !stopWordsDictionary.contains(word)) { //tova li znachi da q ignoriram ot vh. text????/
                        mistakes.add(new Mistake(lineNumber, word));
                    }

                }

                lineNumber++;
            }

            return mistakes;
        } catch (IOException exception) {
            return null; //EXCEPTION-I!
        }
    }
}
