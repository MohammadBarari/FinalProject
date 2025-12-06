package org.example.exeptions.password;

public class AllNotBeLetterOrDigits extends PasswordException {
    public  AllNotBeLetterOrDigits() {
        super("All not be letter or digits");
    }
}
