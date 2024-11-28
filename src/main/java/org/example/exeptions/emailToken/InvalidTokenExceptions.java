package org.example.exeptions.emailToken;

public class InvalidTokenExceptions extends RuntimeException {
    public InvalidTokenExceptions(String message) {
        super(message);
    }
    public InvalidTokenExceptions(String message, Throwable cause) {
        super(message, cause);
    }
}
