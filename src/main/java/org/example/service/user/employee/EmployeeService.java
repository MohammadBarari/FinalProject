package org.example.service.user.employee;

import org.example.domain.Employee;
import org.example.dto.EmployeeSignUpDto;
import org.example.exeptions.FileIsInvalid;
import org.example.exeptions.ImageSizeIsOver;
import org.example.service.user.BaseUserService;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;

public interface EmployeeService  extends BaseUserService<Employee> {
    void signUpEmployee(EmployeeSignUpDto employee) throws FileNotFoundException;
    boolean validateEmployee(EmployeeSignUpDto employee, File file);
}
