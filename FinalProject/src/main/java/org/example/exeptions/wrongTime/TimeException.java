package org.example.exeptions.wrongTime;

public abstract class TimeException extends RuntimeException {
    public TimeException(String message) {
        super(message);
    }
}
