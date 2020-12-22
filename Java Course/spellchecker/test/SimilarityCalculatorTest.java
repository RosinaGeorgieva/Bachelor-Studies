import org.junit.Test;
import similarwords.SimilarityCoefficient;
import similarwords.SimilarityCoefficientCalculator;

import static org.junit.Assert.assertEquals;

public class SimilarityCalculatorTest { //oshte testki
    private static final String FIRST_WORD = "hello";
    private static final String SECOND_WORD = "help";
    private static final SimilarityCoefficient EXPECTED_SIMILARITY_COEFFICIENT = new SimilarityCoefficient(Math.sqrt(3)/3);

    @Test
    public void testCalculate(){
        assertEquals(EXPECTED_SIMILARITY_COEFFICIENT.simmilarity(), SimilarityCoefficientCalculator.calculate(FIRST_WORD, SECOND_WORD).simmilarity(), 0.01);
    }
}
