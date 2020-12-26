package exceptions;

public enum Exceptions {
    IO_EXCEPTION("IO Error."),
    NULL_ARGUMENT_EXCEPTION("Provided argument is null."),
    NEGATIVE_COUNT_EXCEPTION("Provided argument n for size is negative.");

    private String message;

    private Exceptions(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
