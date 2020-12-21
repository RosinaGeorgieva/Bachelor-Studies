import dictionaries.Dictionary;
import mistake.Mistake;
import mistake.MistakeInspector;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class) // da razshirq testa s poveche case-ove
public class MistakeInspectorTest { //!!! da naprawq konsistentni testobete; obrabotka na dumata predi vikane na klasa?
    private static final String MISTAKEN_WORD = "hallo";
    private static final String CORRECT_WORD = "hello";
    private static final String STOP_WORD = "you";
    private static final String MISTAKEN_INPUT = MISTAKEN_WORD + " " + STOP_WORD;
    private static final String CORRECT_INPUT = CORRECT_WORD + " " + STOP_WORD;
    private static final int MISTAKE_LINE = 1;
    private static Mistake expectedMistake;
    private static Set<Mistake> expectedMistakes;
    private static Set<Mistake> noMistakes;

    @Mock //da gi mock-vam li ili da gi syzdam?
    private Dictionary mainDictionary;

    @Mock
    private Dictionary stopWordsDictionary;

    @BeforeClass
    public static void setUp(){
        expectedMistake= new Mistake(MISTAKE_LINE, MISTAKEN_WORD);
        expectedMistakes = Set.of(expectedMistake);
        noMistakes = new HashSet<>();
    }

    @Test
    public void testInspectWithMistaken(){
        Reader inputReader = new StringReader(MISTAKEN_INPUT);

        when(mainDictionary.contains(MISTAKEN_WORD)).thenReturn(false);
        when(mainDictionary.contains(STOP_WORD)).thenReturn(false);
        when(stopWordsDictionary.contains(MISTAKEN_WORD)).thenReturn(false);
        when(stopWordsDictionary.contains(STOP_WORD)).thenReturn(true);

        assertEquals(expectedMistakes, MistakeInspector.inspect(inputReader, mainDictionary, stopWordsDictionary));
    }

    @Test
    public void testInspectWithCorrect(){
        Reader inputReader = new StringReader(CORRECT_INPUT);

        when(mainDictionary.contains(CORRECT_WORD)).thenReturn(true);
        when(mainDictionary.contains(STOP_WORD)).thenReturn(false);
        when(stopWordsDictionary.contains(CORRECT_WORD)).thenReturn(false);
        when(stopWordsDictionary.contains(STOP_WORD)).thenReturn(true);

        assertEquals(noMistakes, MistakeInspector.inspect(inputReader, mainDictionary, stopWordsDictionary));
    }
}
