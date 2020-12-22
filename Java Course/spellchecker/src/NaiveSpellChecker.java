import dictionaries.MainDictionary;
import dictionaries.DictionaryType;
import metadata.Metadata;
import metadata.MetadataGenerator;
import mistake.Mistake;
import mistake.MistakeInspector;
import similarwords.SimilarWordsDictionary;
import dictionaries.StopWordsDictionary;
import output.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NaiveSpellChecker implements SpellChecker {
    private MainDictionary dictionary;
    private StopWordsDictionary stopWordsDictionary;
    private SimilarWordsDictionary similarWordsDictionary;

    public NaiveSpellChecker(Reader dictionaryReader, Reader stopwordsReader) {
        dictionary = new MainDictionary(dictionaryReader, DictionaryType.MAIN_DICTIONARY);
        stopWordsDictionary = new StopWordsDictionary(stopwordsReader, DictionaryType.STOP_WORDS_DICTIONARY);
        similarWordsDictionary = new SimilarWordsDictionary();
    }

    public NaiveSpellChecker(MainDictionary dictionary, StopWordsDictionary stopWordsDictionary, SimilarWordsDictionary similarWordsDictionary){
        this.dictionary = dictionary;
        this.stopWordsDictionary = stopWordsDictionary;
        this.similarWordsDictionary = similarWordsDictionary;
    }

    @Override
    public void analyze(Reader textReader, Writer output, int suggestionsCount) throws IOException { //za vseki analiz nov set ot greshki
        String text = new BufferedReader(textReader).lines().collect(Collectors.joining());
        Set<Mistake> mistakes = MistakeInspector.inspect(text, dictionary, stopWordsDictionary);
        for (Mistake mistake : mistakes) {
            similarWordsDictionary.getNMostSimilar(mistake.word(), suggestionsCount, dictionary); //tuk veche sme si slojili predlojeniqta v map-a
        }
        Metadata metadata = MetadataGenerator.generate(text, dictionary, stopWordsDictionary);
        SpellcheckOutput spellcheckOutput = new SpellcheckOutput(text, metadata, mistakes, similarWordsDictionary.getSimilarByCoefficientByWord());
        SpellcheckOutputFormatter.generate(spellcheckOutput, suggestionsCount, output, dictionary, similarWordsDictionary);
        //textReader close?
    }

    @Override
    public Metadata metadata(Reader textReader) {
        String text = new BufferedReader(textReader).lines().collect(Collectors.joining());
        return MetadataGenerator.generate(text, dictionary, stopWordsDictionary);
        //textReader close?
    }

    @Override
    public List<String> findClosestWords(String word, int n) {
        return similarWordsDictionary.getNMostSimilar(word, n, dictionary);
    }
}

