package org.example.exeptions.password;

public class PasswordOrUserNameWrong extends PasswordException{
    public PasswordOrUserNameWrong(){
        super("please enter all field correctly");
    }
}
