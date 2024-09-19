package org.example.service.user.employee;

import org.example.domain.Employee;
import org.example.domain.EmployeeImages;
import org.example.dto.EmployeeSignUpDto;
import org.example.exeptions.FileIsInvalid;
import org.example.exeptions.ImageSizeIsOver;
import org.example.service.user.BaseUserService;

import java.awt.*;

public interface EmployeeService  extends BaseUserService<Employee> {
    void signUpEmployee(EmployeeSignUpDto employee);
    boolean validateEmployee(EmployeeSignUpDto employee);
    boolean checkImageSize(EmployeeImages image) throws ImageSizeIsOver, FileIsInvalid;
}
