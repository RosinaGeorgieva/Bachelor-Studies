package spellchecker;

import dictionaries.AbstractDictionary;
import mistake.Mistake;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import spellchecker.Metadata;
import spellchecker.NaiveSpellChecker;
import suggestion.SuggestionsRepository;

import java.io.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NaiveSpellCheckerTest {
    private static final int VALID_SUGGESTION_COUNT = 2;
    private static final int INVALID_SUGGESTION_COUNT = -1;
    private static final int ZERO_SUGGESTION_COUNT = 0;
    private static final String OUTPUT_FORMAT = "%s%n%s%n%s%n%s%n%s";
    private static final String METADATA_HEADING = "= = = Metadata = = =";
    private static final String FINDINGS_HEADING = "= = = Findings = = =";
    private static final String NO_SUGGESTIONS_FOUND = "No suggestions found.";
    private static final String EMPTY_WORD = "";

    private static final String INPUT_1 = "";
    private static final int NUMBER_OF_CHARACTERS_1 = 0;
    private static final int NUMBER_OF_WORDS_1 = 0;
    private static final int NUMBER_OF_MISTAKES_1 = 0;
    private static final Metadata METADATA_1 = new Metadata(NUMBER_OF_CHARACTERS_1, NUMBER_OF_WORDS_1, NUMBER_OF_MISTAKES_1);
    private static final String EXPECTED_1 = new Formatter().format(OUTPUT_FORMAT, INPUT_1, METADATA_HEADING, METADATA_1.toString(), FINDINGS_HEADING, NO_SUGGESTIONS_FOUND).toString();

    private static final String INPUT_2 = new Formatter().format("%s%n%s", "Hallo, there, deer!", "Haw have you been?").toString();
    private static final int NUMBER_OF_CHARACTERS_2 = 32;
    private static final int NUMBER_OF_WORDS_2 = 5;
    private static final int NUMBER_OF_MISTAKES_2 = 3;
    private static final Metadata METADATA_2 = new Metadata(NUMBER_OF_CHARACTERS_2, NUMBER_OF_WORDS_2, NUMBER_OF_MISTAKES_2);
    private static final String MISTAKEN_WORD_1 = "hallo";
    private static final String MISTAKEN_WORD_2 = "deer";
    private static final String MISTAKEN_WORD_3 = "haw";
    private static final String CORRECT_WORD_1 = "have";
    private static final String CORRECT_WORD_2 = "been";
    private static final String STOP_WORD_1 = "there";
    private static final String STOP_WORD_2 = "you";
    private static final Mistake MISTAKE_1 = new Mistake(1, MISTAKEN_WORD_1);
    private static final Mistake MISTAKE_2 = new Mistake(1, MISTAKEN_WORD_2);
    private static final Mistake MISTAKE_3 = new Mistake(2, MISTAKEN_WORD_3);
    private static final ArrayList<String> SUGGESTIONS_1 = new ArrayList<>(List.of("help", "hello"));
    private static final ArrayList<String> SUGGESTIONS_2 = new ArrayList<>(List.of("dear", "eerie"));
    private static final ArrayList<String> SUGGESTIONS_3 = new ArrayList<>(List.of("how", "ham", "law", "paw"));
    private static final Map<String, ArrayList<String>> SUGGESTIONS_BY_WORD = new LinkedHashMap<>(Map.of(MISTAKE_1.word(), SUGGESTIONS_1, MISTAKE_2.word(), SUGGESTIONS_2, MISTAKE_3.word(), SUGGESTIONS_3));
    private static final String LINE_1 = "Line #1, {deer} - Possible suggestions are {dear, eerie}";
    private static final String LINE_2 = "Line #1, {hallo} - Possible suggestions are {help, hello}";
    private static final String LINE_3 = "Line #2, {haw} - Possible suggestions are {how, ham, law, paw}";
    private static final String SUGGESTIONS_FORMATTING_1 = new Formatter().format("%s%n%s%n%s", LINE_1, LINE_2, LINE_3).toString();
    private static final String EXPECTED_2 = new Formatter().format(OUTPUT_FORMAT, INPUT_2, METADATA_HEADING, METADATA_2.toString(), FINDINGS_HEADING, SUGGESTIONS_FORMATTING_1).toString();

    private static final String INPUT_3 = MISTAKEN_WORD_1;
    private static final List<String> EXPECTED_3 = SUGGESTIONS_1;

    private static final String INPUT_4 = "Hallo, there!";
    private static final int NUMBER_OF_CHARACTERS_4 = 12;
    private static final int NUMBER_OF_WORDS_4 = 1;
    private static final int NUMBER_OF_MISTAKES_4 = 1;
    private static final Metadata METADATA_4 = new Metadata(NUMBER_OF_CHARACTERS_4, NUMBER_OF_WORDS_4, NUMBER_OF_MISTAKES_4);
    private static final String LINE_4 = "Line #1, {hallo} - Possible suggestions are {}";
    private static final String EXPECTED_4 = new Formatter().format(OUTPUT_FORMAT, INPUT_4, METADATA_HEADING, METADATA_4.toString(), FINDINGS_HEADING, LINE_4).toString();

    @Mock
    private AbstractDictionary basicDictionary;

    @Mock
    private AbstractDictionary stopWordsDictionary;

    @Mock
    private SuggestionsRepository suggestionsRepository;

    private NaiveSpellChecker naiveSpellChecker;

    @Before
    public void setUp() {
        naiveSpellChecker = new NaiveSpellChecker(basicDictionary, stopWordsDictionary, suggestionsRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnalyzeNullReader() {
        Reader inputReader = null;
        Writer outputWriter = new StringWriter();

        naiveSpellChecker.analyze(inputReader, outputWriter, VALID_SUGGESTION_COUNT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnalyzeNullWriter() {
        Reader inputReader = new StringReader(INPUT_1);
        Writer outputWriter = null;

        naiveSpellChecker.analyze(inputReader, outputWriter, VALID_SUGGESTION_COUNT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnalyzeNegativeCount() {
        Reader inputReader = new StringReader(INPUT_1);
        Writer outputWriter = new StringWriter();

        naiveSpellChecker.analyze(inputReader, outputWriter, INVALID_SUGGESTION_COUNT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMetadataNullReader() {
        Reader inputReader = null;

        naiveSpellChecker.metadata(inputReader);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClosestWordsNullWord() {
        String word = null;

        naiveSpellChecker.findClosestWords(word, VALID_SUGGESTION_COUNT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClosestWordsNegativeCount() {
        String word = INPUT_1;

        naiveSpellChecker.findClosestWords(word, INVALID_SUGGESTION_COUNT);
    }

    @Test
    public void testAnalyzeEmptyText() {
        Reader inputReader = new StringReader(INPUT_1);
        Writer outputWriter = new StringWriter();

        when(basicDictionary.contains(EMPTY_WORD)).thenReturn(false);
        when(stopWordsDictionary.contains(EMPTY_WORD)).thenReturn(false);
        when(suggestionsRepository.getDictionary()).thenReturn(basicDictionary);
        when(suggestionsRepository.getSuggestionsByCoefficientByWord()).thenReturn(new LinkedHashMap<>());
        when(suggestionsRepository.getNSuggestionsForWord(EMPTY_WORD, VALID_SUGGESTION_COUNT)).thenReturn(new ArrayList<>());

        naiveSpellChecker.analyze(inputReader, outputWriter, VALID_SUGGESTION_COUNT);
        assertEquals(EXPECTED_1, outputWriter.toString());
    }

    @Test
    public void testAnalyze() {
        Reader inputReader = new StringReader(INPUT_2);
        Writer outputWriter = new StringWriter();

        when(basicDictionary.contains(CORRECT_WORD_1)).thenReturn(true);
        when(basicDictionary.contains(CORRECT_WORD_2)).thenReturn(true);
        when(basicDictionary.contains(MISTAKEN_WORD_1)).thenReturn(false);
        when(basicDictionary.contains(MISTAKEN_WORD_2)).thenReturn(false);
        when(basicDictionary.contains(MISTAKEN_WORD_3)).thenReturn(false);
        when(basicDictionary.contains(STOP_WORD_1)).thenReturn(false);
        when(basicDictionary.contains(STOP_WORD_2)).thenReturn(false);
        when(stopWordsDictionary.contains(CORRECT_WORD_1)).thenReturn(false);
        when(stopWordsDictionary.contains(CORRECT_WORD_2)).thenReturn(false);
        when(stopWordsDictionary.contains(MISTAKEN_WORD_1)).thenReturn(false);
        when(stopWordsDictionary.contains(MISTAKEN_WORD_2)).thenReturn(false);
        when(stopWordsDictionary.contains(MISTAKEN_WORD_3)).thenReturn(false);
        when(stopWordsDictionary.contains(STOP_WORD_1)).thenReturn(true);
        when(stopWordsDictionary.contains(STOP_WORD_2)).thenReturn(true);
        when(suggestionsRepository.getDictionary()).thenReturn(basicDictionary);
        when(suggestionsRepository.getNSuggestionsForWord(MISTAKEN_WORD_1, VALID_SUGGESTION_COUNT)).thenReturn(SUGGESTIONS_1);
        when(suggestionsRepository.getNSuggestionsForWord(MISTAKEN_WORD_2, VALID_SUGGESTION_COUNT)).thenReturn(SUGGESTIONS_2);
        when(suggestionsRepository.getNSuggestionsForWord(MISTAKEN_WORD_3, VALID_SUGGESTION_COUNT)).thenReturn(SUGGESTIONS_3);

        naiveSpellChecker.analyze(inputReader, outputWriter, VALID_SUGGESTION_COUNT);
        assertEquals(EXPECTED_2, outputWriter.toString());
    }

    @Test
    public void testMetadata() {
        Reader inputReader = new StringReader(INPUT_2);

        when(basicDictionary.contains(CORRECT_WORD_1)).thenReturn(true);
        when(basicDictionary.contains(CORRECT_WORD_2)).thenReturn(true);
        when(basicDictionary.contains(MISTAKEN_WORD_1)).thenReturn(false);
        when(basicDictionary.contains(MISTAKEN_WORD_2)).thenReturn(false);
        when(basicDictionary.contains(MISTAKEN_WORD_3)).thenReturn(false);
        when(basicDictionary.contains(STOP_WORD_1)).thenReturn(false);
        when(basicDictionary.contains(STOP_WORD_2)).thenReturn(false);
        when(stopWordsDictionary.contains(CORRECT_WORD_1)).thenReturn(false);
        when(stopWordsDictionary.contains(CORRECT_WORD_2)).thenReturn(false);
        when(stopWordsDictionary.contains(MISTAKEN_WORD_1)).thenReturn(false);
        when(stopWordsDictionary.contains(MISTAKEN_WORD_2)).thenReturn(false);
        when(stopWordsDictionary.contains(MISTAKEN_WORD_3)).thenReturn(false);
        when(stopWordsDictionary.contains(STOP_WORD_1)).thenReturn(true);
        when(stopWordsDictionary.contains(STOP_WORD_2)).thenReturn(true);

        assertEquals(METADATA_2, naiveSpellChecker.metadata(inputReader));
    }

    @Test
    public void testFindClosest() {
        when(suggestionsRepository.getNSuggestionsForWord(MISTAKEN_WORD_1, VALID_SUGGESTION_COUNT)).thenReturn(SUGGESTIONS_1);

        assertEquals(EXPECTED_3, naiveSpellChecker.findClosestWords(INPUT_3, VALID_SUGGESTION_COUNT));
    }

    @Test
    public void testAnalyzeZeroCount() {
        Reader inputReader = new StringReader(INPUT_4);
        Writer outputWriter = new StringWriter();

        when(basicDictionary.contains(MISTAKEN_WORD_1)).thenReturn(false);
        when(basicDictionary.contains(STOP_WORD_1)).thenReturn(false);
        when(stopWordsDictionary.contains(MISTAKEN_WORD_1)).thenReturn(false);
        when(stopWordsDictionary.contains(STOP_WORD_1)).thenReturn(true);
        when(suggestionsRepository.getDictionary()).thenReturn(basicDictionary);
        when(suggestionsRepository.getNSuggestionsForWord(MISTAKEN_WORD_1, ZERO_SUGGESTION_COUNT)).thenReturn(new ArrayList<>());

        naiveSpellChecker.analyze(inputReader, outputWriter, ZERO_SUGGESTION_COUNT);
        assertEquals(EXPECTED_4, outputWriter.toString());
    }

    //find closests za duma ot rechnika
}
