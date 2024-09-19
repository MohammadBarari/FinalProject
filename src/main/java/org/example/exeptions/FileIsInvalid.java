package org.example.exeptions;

public class FileIsInvalid extends Exception{
    public FileIsInvalid(){
        super("File is not valid");
    }
}
