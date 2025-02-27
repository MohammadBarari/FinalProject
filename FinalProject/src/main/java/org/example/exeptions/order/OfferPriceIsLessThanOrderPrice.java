package org.example.exeptions.order;

public class OfferPriceIsLessThanOrderPrice extends OrderException{
    public OfferPriceIsLessThanOrderPrice(){
        super("your offer price is less than order price");
    }
}
