import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class App {
    private static String test = "A hello,  ^^baby, you're so! palka$$.\n";

    public static void main(String[] args) throws IOException {
        Path dictionaryPath = Path.of("C:\\Users\\Acer\\eclipse-workspace\\hw2_first_try\\src\\dictionary.txt");
        Reader dictionaryRreader = new FileReader(dictionaryPath.toFile());
        Path stopwordsPath = Path.of("C:\\Users\\Acer\\eclipse-workspace\\hw2_first_try\\src\\stopwords.txt");
        Reader stopwordsReader = new FileReader(stopwordsPath.toFile());
        NaiveSpellChecker checker = new NaiveSpellChecker(dictionaryRreader, stopwordsReader);
        Reader text = new CharArrayReader(test.toCharArray());
        System.out.println(checker.metadata(text)); //SUUUUUUUPER BAVNO
    }
}
