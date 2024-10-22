package org.example.exeptions;

public class UnableToChangePassWord extends RuntimeException {
    public UnableToChangePassWord(String message) {
        super(message);
    }
    public UnableToChangePassWord(String message, Throwable cause) {
        super(message, cause);
    }
}
