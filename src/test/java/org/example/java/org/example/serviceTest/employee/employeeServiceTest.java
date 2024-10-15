package org.example.java.org.example.serviceTest.employee;

import jakarta.validation.Validator;
import org.example.domain.Employee;
import org.example.domain.PassAndUser;
import org.example.dto.EmployeeSignUpDto;
import org.example.exeptions.DontHaveEnoughMoney;
import org.example.exeptions.PassNot8Digits;
import org.example.repository.user.employee.EmployeeRepository;

import org.example.service.user.employee.imp.EmployeeServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class employeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private Validator validator;

    @InjectMocks
    private EmployeeServiceImp employeeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void signUpEmployeeTest() throws IOException {
        EmployeeSignUpDto employeeSignUpDto = new EmployeeSignUpDto("","Barari","@@mbarari@gmail.com",
                "09335212399","138013za","C:\\Users\\ASUS\\Pictures\\Camera Roll\\WIN_20241007_20_30_32_Pro.jpg");
        System.out.println("dto works fine");
        employeeService.signUpEmployee(employeeSignUpDto);
        verify(employeeRepository).save(any(Employee.class),any(PassAndUser.class));
    }

    @Test
    public void passWordCheckError() throws IOException {
        EmployeeSignUpDto employeeSignUpDto = new EmployeeSignUpDto("Mohammad","Barari","mbarari@gmail.com",
                "09335212399","1384664013za","C:\\Users\\ASUS\\Pictures\\Screenshots\\Screenshot 2024-10-07 18574.png");
        assertThrows(PassNot8Digits.class,()->{
            employeeService.signUpEmployee(employeeSignUpDto);

        });
    }
    @Test
    public void checkIfEmailIsValid()throws IOException {
        EmployeeSignUpDto employeeSignUpDto = new EmployeeSignUpDto("Mohammad","Barari","mbarari@gmail.com",
                "09335212399","138046za","C:\\Users\\ASUS\\Pictures\\Camera Roll\\WIN_20241007_20_30_32_Pro.jpg");
        assertThrows(DontHaveEnoughMoney.class,()->{
            employeeService.signUpEmployee(employeeSignUpDto);
        });
    }
}
