package org.example.exeptions;

public class EmployeeIsNotAccepted extends Exception{
    public EmployeeIsNotAccepted(){
        super("Employee is not accepted");
    }
}
