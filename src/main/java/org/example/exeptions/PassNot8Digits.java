package org.example.exeptions;

public class PassNot8Digits extends RuntimeException {
    public PassNot8Digits() {
        super("password should be 8 digits");
    }
}
