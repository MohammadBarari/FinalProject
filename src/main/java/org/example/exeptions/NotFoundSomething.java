package org.example.exeptions;

public class NotFoundSomething extends RuntimeException{
    public NotFoundSomething(String message) {
        super(message);
    }
    public NotFoundSomething(String message,Throwable throwable) {
        super(message ,throwable);
    }
}
