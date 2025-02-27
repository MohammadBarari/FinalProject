package org.example.exeptions.global;

public class FailedDoingOperation extends RuntimeException {
    public FailedDoingOperation(String message) {
        super(message);
    }
    public FailedDoingOperation(String message, Throwable cause) {
        super(message, cause);
    }
}
