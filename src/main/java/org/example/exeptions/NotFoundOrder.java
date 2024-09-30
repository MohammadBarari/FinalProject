package org.example.exeptions;

public class NotFoundOrder extends Exception {
    public NotFoundOrder() {
        super("Not found your specific order");
    }
}
