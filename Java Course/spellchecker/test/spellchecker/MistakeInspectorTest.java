package spellchecker;

import dictionaries.AbstractDictionary;
import mistake.Mistake;
import mistake.MistakeInspector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MistakeInspectorTest {
    private static final String INPUT_1 = "hAllo, you," + System.lineSeparator() +  "babi!";
    private static final String MISTAKEN_WORD_1 = "hallo";
    private static final String MISTAKEN_WORD_2 = "babi";
    private static final String STOP_WORD = "you";
    private static final int MISTAKE_LINE_1 = 1;
    private static final int MISTAKE_LINE_2 = 2;
    private static final Mistake MISTAKE_1 = new Mistake(MISTAKE_LINE_1, MISTAKEN_WORD_1);
    private static final Mistake MISTAKE_2 = new Mistake(MISTAKE_LINE_2, MISTAKEN_WORD_2);
    private static final List<Mistake> EXPECTED_1 = List.of(MISTAKE_1, MISTAKE_2);

    private static final String INPUT_2 = "hello, you!";
    private static final String CORRECT_WORD_1 = "hello";
    private static final List<Mistake> EXPECTED_2 = new ArrayList<>();

    private static final String INPUT_3 = "-**%, you!";
    private static final String CORRECT_WORD_2 = "-**%";
    private static final List<Mistake> EXPECTED_3 = new ArrayList<>();

    private static final String INPUT_4 = "Hallo, you, babi!";
    private static final Mistake MISTAKE_3 = new Mistake(MISTAKE_LINE_1, MISTAKEN_WORD_2);
    private static final List<Mistake> EXPECTED_4 = List.of(MISTAKE_1, MISTAKE_3);

    private static final String INPUT_5 = "Hallo, hallo you!";
    private static final List<Mistake> EXPECTED_5 = List.of(MISTAKE_1);

    private static final String INPUT_6 = "Hallo," + System.lineSeparator() + "hallo, you!";
    private static final Mistake MISTAKE_4 = new Mistake(MISTAKE_LINE_2, MISTAKEN_WORD_1);
    private static final List<Mistake> EXPECTED_6 = List.of(MISTAKE_1, MISTAKE_4);

    private static final String INPUT_7 = "";
    private static final String EMPTY_STRING = "";
    private static final List<Mistake> EXPECTED_7 = new ArrayList<>();
//VARARGS POMOSHTNI FUNKCII
    @Mock
    private AbstractDictionary dictionary;

    @Mock
    private AbstractDictionary stopWordsDictionary;

    @Test
    public void testInspectWithCorrectInput(){
        mockDictionariesCorrectInput(CORRECT_WORD_1, STOP_WORD);
        assertEquals(EXPECTED_2, MistakeInspector.inspect(INPUT_2, dictionary, stopWordsDictionary));
    }

    @Test
    public void testInspectNonalphanumericalsAreNotMistakes(){
        mockDictionariesCorrectInput(CORRECT_WORD_2, STOP_WORD);
        assertEquals(EXPECTED_3, MistakeInspector.inspect(INPUT_3, dictionary, stopWordsDictionary));
    }

    @Test
    public void testInspectWithSingleMistakenOnMultipleLines(){
        mockDictionariesWrongInput(MISTAKEN_WORD_1, MISTAKEN_WORD_2, STOP_WORD);
        assertEquals(EXPECTED_1, MistakeInspector.inspect(INPUT_1, dictionary, stopWordsDictionary));
    }

    @Test
    public void testInspectWithMultipleMistakenOnSameLine(){
        mockDictionariesWrongInput(MISTAKEN_WORD_1, MISTAKEN_WORD_2, STOP_WORD);
        assertEquals(EXPECTED_4, MistakeInspector.inspect(INPUT_4, dictionary, stopWordsDictionary));
    }

    @Test
    public void testInspectWithSameMistakeOnSameLine(){
        mockDictionariesWrongInput(MISTAKEN_WORD_1, MISTAKEN_WORD_2, STOP_WORD);
        assertEquals(EXPECTED_5, MistakeInspector.inspect(INPUT_5, dictionary, stopWordsDictionary));
    }

    @Test
    public void testInspectWithSameMistakesOnMultipleLines(){
        mockDictionariesWrongInput(MISTAKEN_WORD_1, MISTAKEN_WORD_2, STOP_WORD);
        assertEquals(EXPECTED_6, MistakeInspector.inspect(INPUT_6, dictionary, stopWordsDictionary));
    }

    @Test
    public void testInspectWithEmptyInput(){
        mockDictionariesCorrectInput(EMPTY_STRING, EMPTY_STRING);
        assertEquals(EXPECTED_7, MistakeInspector.inspect(INPUT_7, dictionary, stopWordsDictionary));
    }

    private void mockDictionariesCorrectInput(String correctWord, String stopWord) {
        when(dictionary.contains(correctWord)).thenReturn(true);
        when(dictionary.contains(stopWord)).thenReturn(false);
        when(stopWordsDictionary.contains(correctWord)).thenReturn(false);
        when(stopWordsDictionary.contains(stopWord)).thenReturn(true);
    }

    private void mockDictionariesWrongInput(String firstWrongWord, String secondWrongWord, String stopWord){
        when(dictionary.contains(firstWrongWord)).thenReturn(false);
        when(dictionary.contains(secondWrongWord)).thenReturn(false);
        when(dictionary.contains(stopWord)).thenReturn(false);
        when(stopWordsDictionary.contains(firstWrongWord)).thenReturn(false);
        when(stopWordsDictionary.contains(secondWrongWord)).thenReturn(false);
        when(stopWordsDictionary.contains(stopWord)).thenReturn(true);
    }
}
