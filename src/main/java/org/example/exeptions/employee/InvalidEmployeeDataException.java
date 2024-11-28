package org.example.exeptions.employee;

public class InvalidEmployeeDataException extends EmployeeException {
    public InvalidEmployeeDataException(String message) {
        super(message);
    }
}
