package spellchecker;

import org.junit.Test;
import similarwords.SimilarityCoefficientCalculator;

import static org.junit.Assert.assertEquals;

public class SimilarityCalculatorTest {
    private static final String WORD_1 = "hello";
    private static final String WORD_2 = "help";
    private static final Double EXPECTED_1 = Math.sqrt(3)/3;

    private static final String WORD_3 = "zombie";
    private static final Double EXPECTED_2 = Double.valueOf(0);

    @Test
    public void testCalculatePositiveSimilarity(){
        assertEquals(EXPECTED_1, SimilarityCoefficientCalculator.calculate(WORD_1, WORD_2).similarity(), 0.01);
    }

    @Test
    public void testCalculateZeroSimilarity() {
        assertEquals(EXPECTED_2, SimilarityCoefficientCalculator.calculate(WORD_1, WORD_3).similarity(), 0.01);
    }
}
