package org.example.exeptions;

public class OfferPriceIsLessThanOrderPrice extends RuntimeException{
    public OfferPriceIsLessThanOrderPrice(){
        super("your offer price is less than order price");
    }
}
