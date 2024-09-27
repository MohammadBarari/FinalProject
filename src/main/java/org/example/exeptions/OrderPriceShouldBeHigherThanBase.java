package org.example.exeptions;

public class OrderPriceShouldBeHigherThanBase extends Exception{
    public OrderPriceShouldBeHigherThanBase(){
        super("OrderPriceShouldBeHigherThanBase");
    }
}
