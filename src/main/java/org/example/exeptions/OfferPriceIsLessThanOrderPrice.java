package org.example.exeptions;

public class OfferPriceIsLessThanOrderPrice extends Exception{
    public OfferPriceIsLessThanOrderPrice(){
        super("OfferPriceIsLessThanOrderPrice");
    }
}
