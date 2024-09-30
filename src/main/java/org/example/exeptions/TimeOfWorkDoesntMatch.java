package org.example.exeptions;

public class TimeOfWorkDoesntMatch extends Exception{
    public TimeOfWorkDoesntMatch(){
        super("time of work does not match");
    }
}
