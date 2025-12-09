package org.example.java.org.example.service;

import org.example.domain.Employee;
import org.example.dto.employee.EmployeeSignUpDto;
import org.example.enumirations.TypeOfUser;
import org.example.events.UserCreationEvent;
import org.example.repository.user.employee.EmployeeRepository;
import org.example.service.user.employee.imp.EmployeeServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.token.TokenService;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImpTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ApplicationEventPublisher publisher;

    @Mock
    private TokenService tokenService; // whatever your sendToken uses

    @InjectMocks
    private EmployeeServiceImp employeeService;

    @Test
    void signUpEmployee_validData_shouldCreateEmployeePublishEventAndSendToken()
    {
        // arrange
        EmployeeSignUpDto dto = new EmployeeSignUpDto(
                "John",
                "Doe",
                "john@example.com",
                "09023335595",
                "123456za",
                "base64-image"
        );
        // act
        EmployeeSignUpDto result = employeeService.signUpEmployee(dto);

        // assert
        assertSame(dto, result);
        verify(publisher).publishEvent(any(UserCreationEvent.class));
        verify(employeeService).sendToken(dto.email(), TypeOfUser.EMPLOYEE);
        verify(employeeRepository).save(any(Employee.class)); // or saveEmployee(...) if you spy
    }


}
