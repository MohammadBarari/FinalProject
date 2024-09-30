package org.example.exeptions;

public class YouInsertNothing extends Exception {
    public YouInsertNothing(){
        super("you have to insert something for change");
    }
}
