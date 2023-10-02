package net.purplegoose.didnb.exeption;

public class StartFailureException extends RuntimeException {
    public StartFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
