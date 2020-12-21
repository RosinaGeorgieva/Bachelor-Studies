import dictionaries.MainDictionary;
import dictionaries.DictionaryType;
import metadata.Metadata;
import metadata.MetadataGenerator;
import mistake.Mistake;
import mistake.MistakeInspector;
import similarwords.SimilarWordsDictionary;
import dictionaries.StopWordsDictionary;
import output.*;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.*;

public class NaiveSpellChecker implements SpellChecker {
    private final MainDictionary dictionary;
    private final StopWordsDictionary stopWordsDictionary;

    public NaiveSpellChecker(Reader dictionaryReader, Reader stopwordsReader) {
        dictionary = new MainDictionary(dictionaryReader, DictionaryType.MAIN_DICTIONARY);
        stopWordsDictionary = new StopWordsDictionary(stopwordsReader, DictionaryType.STOP_WORDS_DICTIONARY);
    }

    @Override
    public void analyze(Reader textReader, Writer output, int suggestionsCount) throws IOException { //za vseki analiz nov set ot greshki
        Set<Mistake> mistakes = MistakeInspector.inspect(textReader, dictionary, stopWordsDictionary);
        for (Mistake mistake : mistakes) {
            SimilarWordsDictionary.getNMostSimilar(mistake.word(), suggestionsCount, dictionary); //tuk veche sme si slojili predlojeniqta v map-a
        }
        SpellcheckOutputFormatter.generate(new SpellcheckOutput(textReader, metadata(textReader), mistakes, SimilarWordsDictionary.getSimilarByCoefficientByWord()), suggestionsCount, output);
    }

    @Override
    public Metadata metadata(Reader textReader) throws IOException {
        return MetadataGenerator.generate(textReader, dictionary, stopWordsDictionary);
    }

    @Override
    public List<String> findClosestWords(String word, int n) {
        return SimilarWordsDictionary.getNMostSimilar(word, n, dictionary);
    }
}

