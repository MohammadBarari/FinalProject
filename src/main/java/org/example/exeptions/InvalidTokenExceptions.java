package org.example.exeptions;

public class InvalidTokenExceptions extends RuntimeException {
    public InvalidTokenExceptions(String message) {
        super(message);
    }
    public InvalidTokenExceptions(String message, Throwable cause) {
        super(message, cause);
    }
}
