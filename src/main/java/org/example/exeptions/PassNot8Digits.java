package org.example.exeptions;

public class PassNot8Digits extends Exception {
    public PassNot8Digits() {
        super("password should be 8 digits");
    }
}
