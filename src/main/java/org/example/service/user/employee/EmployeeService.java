package org.example.service.user.employee;

import org.example.domain.Employee;
import org.example.domain.Orders;
import org.example.domain.PassAndUser;
import org.example.dto.EmployeeSignUpDto;
import org.example.dto.OfferDto;
import org.example.service.user.BaseUserService;

import java.io.File;
import java.io.IOException;

public interface EmployeeService  extends BaseUserService<Employee> {
    EmployeeSignUpDto signUpEmployee(EmployeeSignUpDto employee) ;
    boolean validateEmployee(EmployeeSignUpDto employee, File file);
    OfferDto GiveOfferToOrder(OfferDto offerDto);
    boolean validateIfItCanGetOffer(Orders orders);
    void saveEmployee( Employee employee);
    boolean validateImage(File imageFile);
    boolean checkIfImageSizeIsOkay(File imageFile);

}
