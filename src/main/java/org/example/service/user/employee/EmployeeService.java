package org.example.service.user.employee;

import org.example.domain.Employee;
import org.example.dto.EmployeeSignUpDto;
import org.example.service.user.BaseUserService;

import java.io.File;
import java.io.IOException;

public interface EmployeeService  extends BaseUserService<Employee> {
    void signUpEmployee(EmployeeSignUpDto employee) throws IOException;
    boolean validateEmployee(EmployeeSignUpDto employee, File file);
}
