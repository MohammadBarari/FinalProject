package org.example.exeptions;

public class TimeOfWorkDoesntMatch extends RuntimeException{
    public TimeOfWorkDoesntMatch(){
        super("time of work does not match");
    }
}
