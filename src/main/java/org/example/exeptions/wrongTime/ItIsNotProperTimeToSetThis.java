package org.example.exeptions.wrongTime;

public class ItIsNotProperTimeToSetThis extends TimeException {
    public ItIsNotProperTimeToSetThis() {
        super(" It is not proper time to set this order");
    }
}
