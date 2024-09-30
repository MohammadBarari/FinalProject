package org.example.exeptions;

    public class NotFoundCustomer extends Exception {
    public NotFoundCustomer() {
        super("customer not found");
    }
}
