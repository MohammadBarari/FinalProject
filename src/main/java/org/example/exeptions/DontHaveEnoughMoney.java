package org.example.exeptions;

public class DontHaveEnoughMoney extends Exception{
    public DontHaveEnoughMoney() {
        super("Don't have enough money");
    }
}
