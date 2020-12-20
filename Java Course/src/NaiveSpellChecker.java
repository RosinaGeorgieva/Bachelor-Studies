import dictionaries.Dictionary;
import dictionaries.StopWordsDictionary;
import util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.*;

public class NaiveSpellChecker implements  SpellChecker{
    private final Dictionary dictionary;
    private final StopWordsDictionary stopWordsDictionary;
    private final Map<String, TreeMap<Double, ArrayList<String>>> suggestedWordsByMistake;

    public NaiveSpellChecker(Reader dictionaryReader, Reader stopwordsReader){
        this.dictionary = Dictionary.compose(dictionaryReader);
        this.stopWordsDictionary = StopWordsDictionary.compose(stopwordsReader);
        this.suggestedWordsByMistake = new HashMap<>();
    }

    @Override
    public void analyze(Reader textReader, Writer output, int suggestionsCount) { //za vseki analiz nov set ot greshki
        Set<Mistake> mistakes = findMistakes(textReader);
        for(Mistake mistake : mistakes){
            generateSuggestions(mistake.word()); //tuk veche sme si slojili predlojeniqta v map-a
        }
        output = OutputGenerator.generate(new SpellCheckerResult(textReader, mistakes, suggestedWordsByMistake), suggestionsCount);
    }

    @Override
    public Metadata metadata(Reader textReader) {
        return MetadataGenerator.generate(textReader, dictionary, stopWordsDictionary);
    }

    @Override
    public List<String> findClosestWords(String word, int n) {
        Collection<ArrayList<String>> suggestedWords = suggestedWordsByMistake.get(word).values();
        List<String> mergedListOfSuggested = new ArrayList<>();
        for(ArrayList<String> nextSuggestedWords : suggestedWords){
            mergedListOfSuggested.addAll(nextSuggestedWords);
        }
        if(mergedListOfSuggested.size() < n){
            return mergedListOfSuggested;
        }
        return mergedListOfSuggested.subList(0, n);
    }

    private Set<Mistake> findMistakes(Reader text){
        try(var bufferedReader = new BufferedReader(text)){
            Set<Mistake> mistakes = new HashSet<>();
            String line;
            int lineNumber = 1;
            while ((line = bufferedReader.readLine()) != null) {
                String[] words = WordCleanser.removeAllWhitespaces(line);
                for (String word : words) {
                    word = WordCleanser.makeCaseInsensitive(word);
                    word = WordCleanser.removeBorderNonAlphanumeric(word);

                    if(!dictionary.contains(word) && !stopWordsDictionary.contains(word)){ //tova li znachi da q ignoriram ot vh. text????/
                        mistakes.add(new Mistake(lineNumber, word));
                    }

                    lineNumber++;
                }
            }

            return mistakes;
        } catch (IOException exception){
            return null; //EXCEPTION-I!
        }
    }

    private void generateSuggestions(String word){
        //
    }
}

