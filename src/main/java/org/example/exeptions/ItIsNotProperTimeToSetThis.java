package org.example.exeptions;

public class ItIsNotProperTimeToSetThis extends RuntimeException {
    public ItIsNotProperTimeToSetThis() {
        super(" It is not proper time to set this order");
    }
}
