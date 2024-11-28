package org.example.exeptions.password;

public class PassNot8Digits extends PasswordException {
    public PassNot8Digits() {
        super("password should be 8 digits");
    }
}
