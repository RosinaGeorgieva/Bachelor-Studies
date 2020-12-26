package spellchecker;

import dictionaries.AbstractDictionary;
import mistake.Mistake;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import output.SpellcheckOutput;
import output.SpellcheckOutputFormatter;
import spellchecker.Metadata;
import suggestion.SuggestionsRepository;

import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SpellcheckOutputFormatterTest {
    private static final int N = 2;
    private static final String OUTPUT_FORMAT = "%s%n%s%n%s%n%s%n%s";
    private static final String METADATA_HEADING = "= = = Metadata = = =";
    private static final String FINDINGS_HEADING = "= = = Findings = = =";
    private static final String NO_SUGGESTIONS_FOUND = "No suggestions found.";

    private static final String INPUT_1 = "Haw are you, \nbabi?";
    private static final String MISTAKEN_WORD_1 = "haw";
    private static final String MISTAKEN_WORD_2 = "babi";
    private static final String CORRECT_WORD_1 = "are";
    private static final String STOP_WORD = "you";
    private static final int MISTAKE_LINE_1 = 1;
    private static final int MISTAKE_LINE_2 = 2;
    private static final Mistake MISTAKE_1 = new Mistake(MISTAKE_LINE_1, MISTAKEN_WORD_1);
    private static final Mistake MISTAKE_2 = new Mistake(MISTAKE_LINE_2, MISTAKEN_WORD_2);
    private static final int NUMBER_OF_CHARACTERS_1 = 15;
    private static final int NUMBER_OF_WORDS_1 = 3;
    private static final int NUMBER_OF_MISTAKES_1 = 2;
    private static final List<Mistake> MISTAKES_1 = List.of(MISTAKE_1, MISTAKE_2);
    private static final Metadata METADATA_1 = new Metadata(NUMBER_OF_CHARACTERS_1, NUMBER_OF_WORDS_1, NUMBER_OF_MISTAKES_1);
    private static final ArrayList<String> SUGGESTED_1 = new ArrayList(List.of("how"));
    private static final ArrayList<String> SUGGESTED_2 = new ArrayList(List.of("baby"));
    private static final Map<String, ArrayList<String>> SUGGESTIONS_BY_WORD_1 = new LinkedHashMap<>(Map.of(MISTAKEN_WORD_1, SUGGESTED_1, MISTAKEN_WORD_2, SUGGESTED_2));
    private static final String SUGGESTIONS_1 = new Formatter().format("%s%n%s", "Line #1, {haw} - Possible suggestions are {how}", "Line #2, {babi} - Possible suggestions are {baby}").toString();
    private static final String EXPECTED_1 = new Formatter().format(OUTPUT_FORMAT, INPUT_1, METADATA_HEADING, METADATA_1.toString(), FINDINGS_HEADING, SUGGESTIONS_1).toString();

    private static final String INPUT_2 = "Hello, you there!";
    private static final String CORRECT_WORD_2 = "hello";
    private static final String CORRECT_WORD_3 = "there";
    private static final int NUMBER_OF_CHARACTERS_2 = 17;
    private static final int NUMBER_OF_WORDS_2 = 2;
    private static final int NUMBER_OF_MISTAKES_2 = 0;
    private static final Metadata METADATA_2 = new Metadata(NUMBER_OF_CHARACTERS_2, NUMBER_OF_WORDS_2, NUMBER_OF_MISTAKES_2);
    private static final List<Mistake> ALL_MISTAKES_2 = new ArrayList<>();
    private static final Map<String, ArrayList<String>> ALL_SUGGESTIONS_2 = new LinkedHashMap<>();
    private static final String EXPECTED_2 = new Formatter().format(OUTPUT_FORMAT, INPUT_2, METADATA_HEADING, METADATA_2.toString(), FINDINGS_HEADING, NO_SUGGESTIONS_FOUND).toString();

    private static final String INPUT_3 = "";
    private static final String EMPTY_WORD = "";
    private static final int NUMBER_OF_CHARACTERS_3 = 0;
    private static final int NUMBER_OF_WORDS_3 = 0;
    private static final int NUMBER_OF_MISTAKES_3 = 0;
    private static final Metadata METADATA_3 = new Metadata(NUMBER_OF_CHARACTERS_3, NUMBER_OF_WORDS_3, NUMBER_OF_MISTAKES_3);
    private static final List<Mistake> ALL_MISTAKES_3 = new ArrayList<>();
    private static final Map<String, ArrayList<String>> ALL_SUGGESTIONS_3 = new LinkedHashMap<>();
    private static final String EXPECTED_3 = new Formatter().format(OUTPUT_FORMAT, INPUT_3, METADATA_HEADING, METADATA_3.toString(), FINDINGS_HEADING, NO_SUGGESTIONS_FOUND).toString();

    private Writer outputWriter;

    @Mock
    private AbstractDictionary dictionary;

    @Mock
    private AbstractDictionary stopWordsDictionary;

    @Mock
    private SuggestionsRepository suggestionsRepository;

    @Before
    public void setUp() {
        outputWriter = new StringWriter();
    }

    @Test
    public void testFormatWithNMistaken() {
        SpellcheckOutput spellcheckOutput = new SpellcheckOutput(INPUT_1, METADATA_1, MISTAKES_1, SUGGESTIONS_BY_WORD_1);

        when(dictionary.contains(MISTAKEN_WORD_1)).thenReturn(false);
        when(dictionary.contains(MISTAKEN_WORD_2)).thenReturn(false);
        when(dictionary.contains(CORRECT_WORD_1)).thenReturn(true);
        when(dictionary.contains(STOP_WORD)).thenReturn(false);
        when(stopWordsDictionary.contains(MISTAKEN_WORD_1)).thenReturn(false);
        when(stopWordsDictionary.contains(MISTAKEN_WORD_2)).thenReturn(false);
        when(stopWordsDictionary.contains(CORRECT_WORD_1)).thenReturn(false);
        when(stopWordsDictionary.contains(STOP_WORD)).thenReturn(true);
        when(suggestionsRepository.getNSuggestionsForWord(MISTAKEN_WORD_1, N)).thenReturn(SUGGESTED_1);
        when(suggestionsRepository.getNSuggestionsForWord(MISTAKEN_WORD_2, N)).thenReturn(SUGGESTED_2);

        SpellcheckOutputFormatter.format(spellcheckOutput, outputWriter);
        assertEquals(EXPECTED_1, outputWriter.toString());
    }

    @Test
    public void testFormatWithCorrect() {
        SpellcheckOutput spellcheckOutput = new SpellcheckOutput(INPUT_2, METADATA_2, ALL_MISTAKES_2, ALL_SUGGESTIONS_2);

        when(dictionary.contains(CORRECT_WORD_2)).thenReturn(true);
        when(dictionary.contains(CORRECT_WORD_3)).thenReturn(true);
        when(dictionary.contains(STOP_WORD)).thenReturn(false);
        when(stopWordsDictionary.contains(CORRECT_WORD_2)).thenReturn(false);
        when(stopWordsDictionary.contains(CORRECT_WORD_3)).thenReturn(true);
        when(stopWordsDictionary.contains(STOP_WORD)).thenReturn(true);

        SpellcheckOutputFormatter.format(spellcheckOutput, outputWriter);
        assertEquals(EXPECTED_2, outputWriter.toString());
    }
//две на една и съща линия и на следващата пак да има
    @Test
    public void testFormatWithEmptyInput(){
        SpellcheckOutput spellcheckOutput = new SpellcheckOutput(INPUT_3, METADATA_3, ALL_MISTAKES_3, ALL_SUGGESTIONS_3);

        when(dictionary.contains(EMPTY_WORD)).thenReturn(true);
        when(dictionary.contains(EMPTY_WORD)).thenReturn(false);

        SpellcheckOutputFormatter.format(spellcheckOutput, outputWriter);
        assertEquals(EXPECTED_3, outputWriter.toString());
    }
}
