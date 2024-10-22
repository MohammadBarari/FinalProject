package org.example.exeptions;

public class OrderPriceShouldBeHigherThanBase extends RuntimeException{
    public OrderPriceShouldBeHigherThanBase(){
        super("order price should be higher than the base");
    }
}
