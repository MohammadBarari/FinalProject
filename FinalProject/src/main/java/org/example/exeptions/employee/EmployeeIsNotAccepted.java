package org.example.exeptions.employee;

public class EmployeeIsNotAccepted extends EmployeeException{
    public EmployeeIsNotAccepted(){
        super("Employee is not accepted");
    }
}
