package org.example.exeptions;

public class ErrorWhileFindingOffers extends RuntimeException {
    public ErrorWhileFindingOffers() {
        super("Error While Finding Offers");
    }
}
