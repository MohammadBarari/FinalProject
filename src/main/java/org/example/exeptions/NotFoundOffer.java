package org.example.exeptions;

public class NotFoundOffer extends Exception{
    public NotFoundOffer() {
        super("Not found your specific offer");
    }
}
