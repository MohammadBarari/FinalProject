package org.example.exeptions;

public class NotFoundOffer extends RuntimeException{
    public NotFoundOffer(String message) {
        super(message);
    }
    public NotFoundOffer(String message,Throwable throwable) {
        super(message ,throwable);
    }

    public NotFoundOffer() {
        super("Not Found");
    }
}
