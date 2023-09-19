package net.purplegoose.didnb.exeption;

public class InvalidMentionException extends RuntimeException {
    public InvalidMentionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMentionException(String message) {
        super(message);
    }
}
