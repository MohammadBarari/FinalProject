package org.example.exeptions;

public class WrongEntrance extends Exception{
    public WrongEntrance(){
        super("please enter all field correctly");
    }
}
