package org.example.exeptions;

public class OrderStateIsNotCorrect extends Exception{
    public OrderStateIsNotCorrect(){
        super("OrderStateIsNotCorrect");
    }
}
