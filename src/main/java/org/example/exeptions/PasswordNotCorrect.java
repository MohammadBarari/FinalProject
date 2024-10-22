package org.example.exeptions;

public class PasswordNotCorrect extends RuntimeException{
    public PasswordNotCorrect(){
        super("password is not corrects");
    }
}
