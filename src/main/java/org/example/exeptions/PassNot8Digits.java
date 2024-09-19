package org.example.exeptions;

public class PassNot8Digits extends Exception {
    public PassNot8Digits() {
        super("pass not 8 digits");
    }
}
