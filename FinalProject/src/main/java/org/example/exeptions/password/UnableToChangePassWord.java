package org.example.exeptions.password;

public class UnableToChangePassWord extends PasswordException {
    public UnableToChangePassWord() {
        super("Unable to change password");
    }
}
