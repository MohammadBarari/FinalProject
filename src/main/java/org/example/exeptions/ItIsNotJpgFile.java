package org.example.exeptions;

public class ItIsNotJpgFile extends RuntimeException {
    public ItIsNotJpgFile(String message) {
        super(message);
    }
}
