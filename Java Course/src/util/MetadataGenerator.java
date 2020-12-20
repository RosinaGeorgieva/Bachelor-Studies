package util;

import dictionaries.Dictionary;
import dictionaries.StopWordsDictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class MetadataGenerator {
    public static Metadata generate(Reader textReader, Dictionary dictionary, StopWordsDictionary stopWordsDictionary) {
        int characterCount = 0;
        int wordCount = 0;
        int mistakesCount = 0;
        try (var bufferedReader = new BufferedReader(textReader)) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] words = WordCleanser.removeAllWhitespaces(line);
                for (String word : words) {
                    word = WordCleanser.makeCaseInsensitive(word);
                    characterCount += word.length();

                    word = WordCleanser.removeBorderNonAlphanumeric(word);

                    if(!stopWordsDictionary.contains(word)){
                        wordCount++;
                    }
                    if(!dictionary.contains(word) && !stopWordsDictionary.contains(word)){
                        mistakesCount++;
                    }
                }
            }

            return new Metadata(characterCount, wordCount, mistakesCount);
        } catch (IOException exception) {
            return null;
        }
    }
}
