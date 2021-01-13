package bg.sofia.uni.fmi.mjt.wish.list.server.exception;

public class NotEnoughArgumentsException extends RuntimeException { //DA SE NAPRAVI CHECKED
    public NotEnoughArgumentsException(String message) {
        super(message);
    }
}
