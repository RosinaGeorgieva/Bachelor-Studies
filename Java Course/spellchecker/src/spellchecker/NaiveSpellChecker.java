package spellchecker;

import dictionaries.AbstractDictionary;
import dictionaries.BasicDictionary;
import dictionaries.DictionaryType;
import exceptions.Exceptions;
import exceptions.RuntimeIOException;
import mistake.Mistake;
import mistake.MistakeInspector;
import suggestion.SuggestionsRepository;
import dictionaries.StopWordsDictionary;
import output.*;
import suggestion.SuggestionsGenerator;
import validator.Validator;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class NaiveSpellChecker implements SpellChecker {
    private AbstractDictionary basicDictionary;
    private AbstractDictionary stopWordsDictionary;
    private SuggestionsRepository suggestionsRepository;

    public NaiveSpellChecker(Reader dictionaryReader, Reader stopwordsReader) {
        this.basicDictionary = new BasicDictionary(new BufferedReader(dictionaryReader).lines().collect(Collectors.joining()), DictionaryType.BASIC_DICTIONARY);
        this.stopWordsDictionary = new StopWordsDictionary(new BufferedReader(stopwordsReader).lines().collect(Collectors.joining()), DictionaryType.STOP_WORDS_DICTIONARY);
        this.suggestionsRepository = new SuggestionsRepository(basicDictionary);
    }

    public NaiveSpellChecker(AbstractDictionary basicDictionary, AbstractDictionary stopWordsDictionary, SuggestionsRepository suggestionsRepository) {
        this.basicDictionary = basicDictionary;
        this.stopWordsDictionary = stopWordsDictionary;
        this.suggestionsRepository = suggestionsRepository;
    }

    @Override
    public void analyze(Reader textReader, Writer output, int suggestionsCount) {
        Validator.checkNull(textReader);
        Validator.checkNull(output);
        Validator.checkNegative(suggestionsCount);

        try (var bufferedReader = new BufferedReader(textReader)) {
            String text = bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
            Metadata metadata = MetadataGenerator.generate(text, basicDictionary, stopWordsDictionary);
            List<Mistake> mistakes = new ArrayList<>(MistakeInspector.inspect(text, basicDictionary, stopWordsDictionary));
            Map<String, ArrayList<String>> suggestionsByWord = suggestionsByWord(mistakes, suggestionsCount);
            SpellcheckOutput spellcheckOutput = new SpellcheckOutput(text, metadata, mistakes, suggestionsByWord);
            SpellcheckOutputFormatter.format(spellcheckOutput, output);
        } catch (IOException exception) {
            throw new RuntimeIOException(Exceptions.IO_EXCEPTION.getMessage());
        }
    }

    @Override
    public Metadata metadata(Reader textReader) {
        Validator.checkNull(textReader);

        try (var bufferedReader = new BufferedReader(textReader)) {
            String text = bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
            return MetadataGenerator.generate(text, basicDictionary, stopWordsDictionary);
        } catch (IOException exception) {
            throw new RuntimeIOException(Exceptions.IO_EXCEPTION.getMessage());
        }
    }

    @Override
    public List<String> findClosestWords(String word, int n) {
        Validator.checkNull(word);
        Validator.checkNegative(n);

        return new ArrayList<>(suggestionsRepository.getNSuggestionsForWord(word, n));
    }

    private Map<String, ArrayList<String>> suggestionsByWord(List<Mistake> mistakes, int suggestionsCount) {
        Map<String, ArrayList<String>> suggestionsByWord = new LinkedHashMap<>();
        for (Mistake mistake : mistakes) {
            SuggestionsGenerator.generate(mistake.word(), suggestionsRepository);
            suggestionsByWord.put(mistake.word(), new ArrayList<>(suggestionsRepository.getNSuggestionsForWord(mistake.word(), suggestionsCount)));
        }
        return suggestionsByWord;
    }
}

