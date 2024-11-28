package org.example.exeptions.password;

public class PasswordNotCorrect extends PasswordException{
    public PasswordNotCorrect(){
        super("password is not corrects");
    }
}
