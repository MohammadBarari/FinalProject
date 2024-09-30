package org.example.exeptions;

public class HandlerIsNull extends Exception{
    public HandlerIsNull(){
        super("handler is empty");
    }
}
