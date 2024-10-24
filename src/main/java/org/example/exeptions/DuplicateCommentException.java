package org.example.exeptions;

public class DuplicateCommentException extends RuntimeException {
    public DuplicateCommentException(String message) {
        super(message);
    }
    public DuplicateCommentException(String message, Throwable cause) {
        super(message, cause);
    }
    public  DuplicateCommentException() {
        super("these parameters are set before ");
    }
}
