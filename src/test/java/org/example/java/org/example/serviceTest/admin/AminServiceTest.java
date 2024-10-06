package org.example.java.org.example.serviceTest.admin;

import org.example.domain.Employee;
import org.example.domain.Handler;
import org.example.domain.SubHandler;
import org.example.enumirations.EmployeeState;
import org.example.exeptions.EmployeeIsNotAccepted;
import org.example.exeptions.HandlerIsNull;
import org.example.exeptions.NotFoundEmployee;
import org.example.repository.user.admin.AdminRepository;
import org.example.service.handler.HandlerService;
import org.example.service.subHandler.SubHandlerService;
import org.example.service.user.admin.imp.AdminServiceImp;
import org.example.service.user.employee.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AminServiceTest {
   @Mock
   private HandlerService handlerService ;
   @Mock
   private SubHandlerService subHandlerService;
   @Mock
   private EmployeeService employeeService;
   @Mock
   private AdminRepository adminRepository;

    @InjectMocks
    AdminServiceImp adminServiceImp;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void saveHandlerTest (){
        Handler handler = new Handler();
        handler.setName("test handler");
        handler.setId(1);
        adminServiceImp.saveHandler(handler);
        verify(handlerService).save(any(Handler.class));
    }
    @Test
    public void saveSubHandlerTest (){
        SubHandler subHandler = new SubHandler();
        Handler handler = new Handler();
        when(handlerService.findHandlerById(1)).thenReturn(handler);
        subHandler.setHandler(handler);
        subHandler.setId(2);
        subHandler.setBasePrice(20d);
        subHandler.setName("subHandler test");
        subHandler.setDetail("detail test");
        adminServiceImp.saveSubHandler(subHandler,1);
        verify(subHandlerService).saveSubHandler(any(SubHandler.class));
    }
    @Test
    public void checkIfItThrowHandlerNullTest(){
        Handler handler = new Handler();
        when(handlerService.findHandlerById(1)).thenReturn(null);
        SubHandler subHandler = new SubHandler();
        assertThrows(HandlerIsNull.class, () -> {
           adminServiceImp.saveSubHandler(subHandler,1);
        });
    }

    @Test
    public void saveEmployeeToSubHandlerTest(){
        Employee employee = new Employee();
        employee.setEmployeeState(EmployeeState.ACCEPTED);
        when(employeeService.findById(1,Employee.class)).thenReturn(employee);
        SubHandler subHandler = new SubHandler();
        when(subHandlerService.findSubHandlerById(1)).thenReturn(subHandler);
        adminServiceImp.saveEmployeeToSubHandler(1,1);
        verify(employeeService).updateUser(any(Employee.class));
        assertTrue(employee.getSubHandlers().contains(subHandler));
    }

    @Test
    void saveEmployeeToSubHandlerNotFountExeptionTest(){
        when(employeeService.findById(1,Employee.class)).thenReturn(null);
        assertThrows(NotFoundEmployee.class, () -> {
            adminServiceImp.saveEmployeeToSubHandler(1,1);
        });
    }
    @Test
    void saveEmployeeToSubHandler(){
        Employee employee = new Employee();
        employee.setEmployeeState(EmployeeState.ACCEPTED);
        when(employeeService.findById(1,Employee.class)).thenReturn(employee);
        when(subHandlerService.findSubHandlerById(1)).thenReturn(null);
        assertThrows(HandlerIsNull.class, () -> {
            adminServiceImp.saveEmployeeToSubHandler(1,1);
        });
    }
    @Test
    void stateNotCorrectExeptionTest(){
        Employee employee = new Employee();
        employee.setEmployeeState(EmployeeState.UNDER_REVIEW);
        when(employeeService.findById(1,Employee.class)).thenReturn(employee);
        assertThrows(EmployeeIsNotAccepted.class,() ->{
            adminServiceImp.saveEmployeeToSubHandler(1,1);
        });
    }
    @Test
    void checkIfTheSubHandlers(){
        Employee employee = new Employee();
        employee.setEmployeeState(EmployeeState.ACCEPTED);
        SubHandler subHandler = new SubHandler();
        Set<SubHandler> subHandlers = new HashSet<>();
        subHandlers.add(subHandler);
        employee.setSubHandlers(subHandlers);
        when(employeeService.findById(1,Employee.class)).thenReturn(employee);
        SubHandler subHandler2 = new SubHandler();
        when(subHandlerService.findSubHandlerById(1)).thenReturn(subHandler2);
        adminServiceImp.saveEmployeeToSubHandler(1,1);
        verify(employeeService).updateUser(employee);
        assertTrue(subHandlers.contains(subHandler));
        assertEquals(2,employee.getSubHandlers().size());
    }
}
