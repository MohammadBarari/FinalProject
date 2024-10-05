package org.example.exeptions;

    public class NotFoundEmployee extends Exception {
    public NotFoundEmployee() {
        super("employee not found");
    }
}
