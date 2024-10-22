package org.example.exeptions;

public class StarShouldBeenBetween1To5 extends RuntimeException{
    public StarShouldBeenBetween1To5(){
        super("star should be between 1 to 5");
    }
}
