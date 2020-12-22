package dictionaries;

import exceptions.Exceptions;
import exceptions.IOException;
import wordcleanser.WordNormalizer;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.stream.Collectors;

public class MainDictionary extends Dictionary {
    public MainDictionary(Reader text, DictionaryType type){
        super(text, type);
    }

    @Override
    protected void compose(Reader text) {
        try(var bufferedReader = new BufferedReader(text)){
            words = bufferedReader.lines().map(WordNormalizer::normalize).filter(word -> !isShort(word)).collect(Collectors.toSet());
        } catch (java.io.IOException exception){
            throw new IOException(Exceptions.IO_EXCEPTION.getMessage());
        }
    }
}
