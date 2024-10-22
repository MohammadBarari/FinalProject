package org.example.exeptions;

public class AllNotBeLetterOrDigits extends RuntimeException {
    public AllNotBeLetterOrDigits() {
        super("All not be letter or digits");
    }
}
