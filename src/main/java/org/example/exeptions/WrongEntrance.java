package org.example.exeptions;

public class WrongEntrance extends RuntimeException{
    public WrongEntrance(){
        super("please enter all field correctly");
    }
}
