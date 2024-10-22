package org.example.exeptions;

public class FailedDoingOperation extends RuntimeException {
    public FailedDoingOperation(String message) {
        super(message);
    }
    public FailedDoingOperation(String message, Throwable cause) {
        super(message, cause);
    }
}
