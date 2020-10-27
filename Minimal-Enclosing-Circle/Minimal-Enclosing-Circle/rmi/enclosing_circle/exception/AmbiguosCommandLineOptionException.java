package rmi.enclosing_circle.exception;

public class AmbiguosCommandLineOptionException extends RuntimeException{
	public AmbiguosCommandLineOptionException(String errorMessage) {
        super(errorMessage);
    }
}
