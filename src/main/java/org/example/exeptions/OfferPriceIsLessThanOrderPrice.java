package org.example.exeptions;

public class OfferPriceIsLessThanOrderPrice extends Exception{
    public OfferPriceIsLessThanOrderPrice(){
        super("your offer price is less than order price");
    }
}
