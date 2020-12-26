package spellchecker;

import org.junit.Test;
import validator.Validator;

public class ValidatorTest {
    @Test(expected = IllegalArgumentException.class)
    public void testCheckNullWithNull() {
        Validator.checkNull(null);
    }

    @Test
    public void testCheckNullWithNotNull() {
        Validator.checkNull(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckNegativeWithNegative(){
        Validator.checkNegative(-1);
    }

    @Test
    public void testCheckNegativeWithNonNegative() {
        Validator.checkNegative(1);
    }
}
