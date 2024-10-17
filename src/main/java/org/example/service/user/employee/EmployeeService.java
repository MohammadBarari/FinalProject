package org.example.service.user.employee;

import org.example.domain.Employee;
import org.example.dto.EmployeeSignUpDto;
import org.example.service.user.BaseUserService;

import java.io.File;
import java.io.IOException;

public interface EmployeeService  extends BaseUserService<Employee> {
    EmployeeSignUpDto signUpEmployee(EmployeeSignUpDto employee) ;
    boolean validateEmployee(EmployeeSignUpDto employee, File file);
}
