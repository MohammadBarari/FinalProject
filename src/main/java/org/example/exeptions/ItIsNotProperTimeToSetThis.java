package org.example.exeptions;

public class ItIsNotProperTimeToSetThis extends Exception {
    public ItIsNotProperTimeToSetThis() {
        super(" It is not proper time to set this order");
    }
}
