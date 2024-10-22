package org.example.exeptions;

public class HandlerIsNull extends RuntimeException{
    public HandlerIsNull(String message) {
        super(message);
    }
    public HandlerIsNull(String message,Throwable throwable) {
        super(message ,throwable);
    }

    public HandlerIsNull() {
        super("Not Found");
    }
}
