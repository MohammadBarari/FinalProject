package org.example.exeptions;

public class SubHandlerNull extends RuntimeException{
    public SubHandlerNull(){
        super("SubHandler is not found ");
    }
}
