package org.example.exeptions;

public class OrderPriceShouldBeHigherThanBase extends Exception{
    public OrderPriceShouldBeHigherThanBase(){
        super("order price should be higher than the base");
    }
}
