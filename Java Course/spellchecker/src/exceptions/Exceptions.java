package exceptions;

public enum Exceptions {
    IO_EXCEPTION("IO Error."),
    NULL_ARGUMENT_EXCEPTION("Provided argument is null."),
    NEGATIVE_SIZE_EXCEPTION("Provided size for output is negative."); //suobshtenieto e tupo

    private String message;

    private Exceptions(String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }
}
