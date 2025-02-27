package org.example.exeptions.order;

public class OrderPriceShouldBeHigherThanBase extends OrderException{
    public OrderPriceShouldBeHigherThanBase(){
        super("order price should be higher than the base");
    }
}
