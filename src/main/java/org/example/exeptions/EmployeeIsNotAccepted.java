package org.example.exeptions;

public class EmployeeIsNotAccepted extends RuntimeException{
    public EmployeeIsNotAccepted(){
        super("Employee is not accepted");
    }
}
