package spellchecker;

import org.junit.Test;

import java.io.*;
import java.util.List;

public class NaiveSpellChekerCompilationTest {
    @Test
    public void compilationTest() throws IOException {
        Reader dictionaryReader = new StringReader(String.join(System.lineSeparator(), List.of("cat", "dog", "bird")));
        Reader soptwordsReader = new StringReader(String.join(System.lineSeparator(), List.of("a", "am", "me")));

        // 1. constructor
        SpellChecker spellChecker = new NaiveSpellChecker(dictionaryReader, soptwordsReader);

        // 2. findClosestWords()
        List<String> closestWords = spellChecker.findClosestWords("hello", 2); //ako hello q nqma v dict, prazen spisuk

        // 3. metadata()
        Reader catTextReader = new StringReader("hello, i am a cat!");
        Metadata metadata = spellChecker.metadata(catTextReader);
        metadata.characters();
        metadata.words();
        metadata.mistakes();

        // 4. analyze()
        Reader dogTextReader = new StringReader("hello, i am a dog!");
        Writer output = new FileWriter("output.txt");
        spellChecker.analyze(dogTextReader, output, 2);
    }
}
