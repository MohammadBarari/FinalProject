package org.example.exeptions.password;

public class YouInsertNothing extends PasswordException {
    public YouInsertNothing(){
        super("you have to insert something for change");
    }
}
