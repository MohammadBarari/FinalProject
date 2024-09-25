package org.example.service.user.employee.imp;

import lombok.SneakyThrows;
import org.example.domain.Credit;
import org.example.domain.Employee;
import org.example.domain.EmployeeImages;
import org.example.domain.PassAndUser;
import org.example.dto.EmployeeSignUpDto;
import org.example.enumirations.TypeOfUser;
import org.example.exeptions.FileIsInvalid;
import org.example.exeptions.ImageSizeIsOver;
import org.example.service.user.BaseUserServiceImp;
import org.example.service.user.employee.EmployeeService;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Objects;

public class EmployeeServiceImp extends BaseUserServiceImp<Employee> implements EmployeeService {
    EmployeeRepository employeeRepository = new EmployeeRepositoryImp();
    @Override
    public void signUpEmployee(EmployeeSignUpDto employeeSignUpDto) {
        if (validateEmployee(employeeSignUpDto)) {
            Employee employee = new Employee();
            employee.setName(employeeSignUpDto.name());
            employee.setEmail(employeeSignUpDto.email());
            employee.setPhone(employeeSignUpDto.phone());
            employee.setLast_name(employeeSignUpDto.last_name());
            employee.setEmployeeImage(employeeSignUpDto.employeeImage());
            employee.setTimeOfRegistration(LocalDateTime.now());
            employee.setScore(0);
            Credit credit = new Credit();
            credit.setTypeOfEmployee(TypeOfUser.EMPLOYEE);
            credit.setAmount(0.0);
            employee.setCredit(credit);
            PassAndUser passAndUser = PassAndUser.builder()
                    .pass(employeeSignUpDto.password())
                    .typeOfUser(TypeOfUser.EMPLOYEE)
                    .username(employeeSignUpDto.phone()).build();
            savePassAndUser(passAndUser);
        }
    }

    @SneakyThrows
    @Override
    public boolean validateEmployee(EmployeeSignUpDto employee) {
        //todo: validate employee
        if (validatePassWord(employee.password())&& checkIfNotDuplicateUser(employee.phone())
        && checkImageSize(employee.employeeImage())){
            return true;
        }
        return false;
        //finished
    }

    @Override
    public boolean checkImageSize(EmployeeImages image) throws ImageSizeIsOver , FileIsInvalid{
        if (image.getFile().exists()){
            long fileSize = image.getFile().length();
            if (fileSize > 300_000L){
                return true;
            }
            else {
                throw new ImageSizeIsOver();
            }
        }
        else {
            throw new FileIsInvalid();
        }
    }

    @Override
    public Employee login(String user, String pass) {
        return employeeRepository.login(user,pass);
    }

    @Override
    public boolean checkIfNotDuplicateUser(String user) {
        if (Objects.isNull(employeeRepository.find(user)))
        {
            return true;
        }
        return false;
    }


}
