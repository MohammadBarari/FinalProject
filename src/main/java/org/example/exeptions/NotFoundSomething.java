package org.example.exeptions;

public class NotFoundSomething extends Exception{
    public NotFoundSomething(String nameOfTheThing) {
        super("not found " + nameOfTheThing);
    }
}
