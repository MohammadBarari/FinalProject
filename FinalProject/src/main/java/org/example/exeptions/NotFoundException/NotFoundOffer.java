package org.example.exeptions.NotFoundException;

public class NotFoundOffer extends NotFoundException{
    public NotFoundOffer(String message) {
        super(message);
    }
    public NotFoundOffer(String message,Throwable throwable) {
        super(message ,throwable);
    }

    public NotFoundOffer() {
        super("Not Found offer");
    }
}
