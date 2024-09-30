package org.example.exeptions;

public class OrderStateIsNotCorrect extends Exception{
    public OrderStateIsNotCorrect(){
        super("order state is not properly correct");
    }
}
