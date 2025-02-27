package org.example.exeptions.money;

public class DontHaveEnoughMoney extends RuntimeException{
    public DontHaveEnoughMoney() {
        super("Don't have enough money");
    }
}
