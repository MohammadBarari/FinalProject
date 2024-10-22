package org.example.exeptions;

public class NotFoundOrder extends RuntimeException {
    public NotFoundOrder(String message) {
        super(message);
    }
    public NotFoundOrder(String message,Throwable throwable) {
        super(message ,throwable);
    }

    public NotFoundOrder() {
        super("Not Found");
    }
}
