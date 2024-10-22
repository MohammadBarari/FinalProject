package org.example.exeptions;

public class InavlidCustomerDataException extends RuntimeException {
    public InavlidCustomerDataException(String message) {
        super(message);
    }
}
