package rmi.enclosing_circle.exception;

public class LackingNumberOfPointsException extends RuntimeException {
	public LackingNumberOfPointsException(String errorMessage) {
        super(errorMessage);
    }
}
