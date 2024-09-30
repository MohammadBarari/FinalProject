package org.example.exeptions;

public class PasswordNotCorrect extends Exception{
    public PasswordNotCorrect(){
        super("password is not corrects");
    }
}
