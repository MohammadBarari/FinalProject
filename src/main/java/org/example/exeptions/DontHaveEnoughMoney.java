package org.example.exeptions;

public class DontHaveEnoughMoney extends RuntimeException{
    public DontHaveEnoughMoney() {
        super("Don't have enough money");
    }
}
