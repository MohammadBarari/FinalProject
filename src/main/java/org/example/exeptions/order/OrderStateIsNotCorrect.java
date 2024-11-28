package org.example.exeptions.order;

public class OrderStateIsNotCorrect extends OrderException{
    public OrderStateIsNotCorrect(){
        super("order state is not properly correct");
    }
}
