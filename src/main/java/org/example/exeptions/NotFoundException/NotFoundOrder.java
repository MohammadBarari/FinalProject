package org.example.exeptions.NotFoundException;

public class NotFoundOrder extends NotFoundException {
    public NotFoundOrder(String message) {
        super(message);
    }
    public NotFoundOrder(String message,Throwable throwable) {
        super(message ,throwable);
    }

    public NotFoundOrder() {
        super("Not Found order");
    }
}
