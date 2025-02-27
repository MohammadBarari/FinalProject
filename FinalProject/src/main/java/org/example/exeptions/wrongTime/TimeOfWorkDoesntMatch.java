package org.example.exeptions.wrongTime;

public class TimeOfWorkDoesntMatch extends TimeException{
    public TimeOfWorkDoesntMatch(){
        super("time of work does not match");
    }
}
