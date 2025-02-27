package org.example.exeptions.employee;

public class ImageValidationException extends EmployeeException {
    public ImageValidationException(String message) {
        super(message);
    }

    public ImageValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

