package org.example.java.org.example.service;

import jakarta.persistence.EntityManager;
import org.example.domain.Employee;
import org.example.dto.employee.EmployeeSignUpDto;
import org.example.enumirations.TypeOfUser;
import org.example.events.UserCreationEvent;
import org.example.exeptions.employee.InvalidEmployeeDataException;
import org.example.repository.user.employee.EmployeeRepository;
import org.example.service.mapStruct.EntityMapper;
import org.example.service.user.employee.imp.EmployeeServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImpTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ApplicationEventPublisher publisher;

    @Mock
    private TokenService tokenService;

    @Mock
    private EntityMapper entityManager;

    @Mock
    private PasswordEncoder passwordEncoder;

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
                "123456za"
        );

        byte[] imageBytes = new byte[]
                {
                        (byte) 0xFF,
                        (byte) 0xD8,
                        0x00,
                        0x00,
                        (byte) 0xFF,
                        (byte) 0xD9
                };

        MultipartFile imageFile = new MockMultipartFile(
                "image",
                "avatar.jpg",
                "image/jpeg",
                imageBytes
        );
        Employee employee = new Employee();
        when(entityManager.dtoToEmployee(dto)).thenReturn(employee);
        when(passwordEncoder.encode(dto.password())).thenReturn("encoded");
        // act
        EmployeeSignUpDto result = employeeService.signUpEmployee(dto,imageFile);

        assertSame(dto, result);
        verify(publisher).publishEvent(any(UserCreationEvent.class));
        verify(employeeRepository).save(any(Employee.class));
    }


}
