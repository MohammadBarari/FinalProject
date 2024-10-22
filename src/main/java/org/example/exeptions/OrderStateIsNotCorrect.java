package org.example.exeptions;

public class OrderStateIsNotCorrect extends RuntimeException{
    public OrderStateIsNotCorrect(){
        super("order state is not properly correct");
    }
}
