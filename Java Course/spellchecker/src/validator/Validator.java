package validator;

import exceptions.Exceptions;

public class Validator {
    public static void checkNull(Object object) {
        if (object == null) {
            throw new IllegalArgumentException(Exceptions.NULL_ARGUMENT_EXCEPTION.getMessage());
        }
    }

    public static void checkNegative(int n) {
        if (n < 0) {
            throw new IllegalArgumentException(Exceptions.NEGATIVE_COUNT_EXCEPTION.getMessage());
        }
    }
}
