package org.example.exeptions.order;

public class StarShouldBeenBetween1To5 extends OrderException{
    public StarShouldBeenBetween1To5(){
        super("star should be between 1 to 5");
    }
}
