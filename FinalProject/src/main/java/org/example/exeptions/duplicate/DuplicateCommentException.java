package org.example.exeptions.duplicate;

public class DuplicateCommentException extends DuplicateException {
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
