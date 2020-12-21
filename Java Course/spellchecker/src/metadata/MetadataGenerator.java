package metadata;

import dictionaries.Dictionary;
import wordcleanser.WordNormalizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class MetadataGenerator {
    public static Metadata generate(Reader textReader, Dictionary dictionary, Dictionary stopWordsDictionary) {
        int characterCount = 0;
        int wordCount = 0;
        int mistakesCount = 0;
        try (var bufferedReader = new BufferedReader(textReader)) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] words = WordNormalizer.removeAllWhitespaces(line);
                for (String word : words) {
                    word = WordNormalizer.makeCaseInsensitive(word);
                    characterCount += word.length();

                    word = WordNormalizer.removeBorderNonAlphanumeric(word);

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
