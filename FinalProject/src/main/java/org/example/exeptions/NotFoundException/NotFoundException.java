package org.example.exeptions.NotFoundException;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("Not found");
    }
    public NotFoundException(String message) {
        super(message);
    }
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
