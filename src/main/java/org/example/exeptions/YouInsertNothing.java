package org.example.exeptions;

public class YouInsertNothing extends RuntimeException {
    public YouInsertNothing(){
        super("you have to insert something for change");
    }
}
