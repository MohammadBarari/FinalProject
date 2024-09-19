package org.example.exeptions;

public class PasswordNotCorrect extends Exception{
    public PasswordNotCorrect(){
        super("Password not correct");
    }
}
