package org.example.java.org.example.service;

import org.example.domain.*;
import org.example.dto.admin.*;
import org.example.dto.orders.OrderOutputDto;
import org.example.dto.servisesDone.DoneDutiesDto;
import org.example.dto.subHandlers.ChangeSubHandlerDto;
import org.example.dto.subHandlers.SubHandlerDto;
import org.example.enumirations.EmployeeState;
import org.example.enumirations.TypeOfUser;
import org.example.exeptions.NotFoundException.NotFoundEmployee;
import org.example.exeptions.NotFoundException.NotFoundHandler;
import org.example.exeptions.NotFoundException.NotFoundSubHandler;
import org.example.exeptions.duplicate.HandlerIsDuplicate;
import org.example.exeptions.employee.EmployeeIsNotAccepted;
import org.example.exeptions.global.FailedDoingOperation;
import org.example.exeptions.password.YouInsertNothing;
import org.example.repository.user.admin.AdminRepository;
import org.example.service.handler.HandlerService;
import org.example.service.mapStruct.EntityMapper;
import org.example.service.subHandler.SubHandlerService;
import org.example.service.user.admin.imp.AdminServiceImp;
import org.example.service.user.customer.CustomerService;
import org.example.service.user.employee.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    @Mock
    private HandlerService handlerService;

    @Mock
    private SubHandlerService subHandlerService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private AdminServiceImp adminService;

    // -----------------------------------
    // saveHandler()
    // -----------------------------------

    @Test
    void saveHandler_shouldSave_whenNameIsNotDuplicate()
    {
        String name = "Cleaning";

        when(handlerService.findHandlerByName(name)).thenReturn(null);

        Handler result = adminService.saveHandler(name);

        assertNotNull(result);
        assertEquals(name, result.getName());

        ArgumentCaptor<Handler> captor = ArgumentCaptor.forClass(Handler.class);
        verify(handlerService).save(captor.capture());
        assertEquals(name, captor.getValue().getName());
    }

    @Test
    void saveHandler_shouldThrowHandlerIsDuplicate_whenNameAlreadyExists()
    {
        String name = "Cleaning";
        Handler existing = Handler.builder()
                .name(name)
                .build();

        when(handlerService.findHandlerByName(name)).thenReturn(existing);

        assertThrows(HandlerIsDuplicate.class,
                () -> adminService.saveHandler(name));

        verify(handlerService, never()).save(any());
    }

    @Test
    void saveSubHandler_shouldSave_whenHandlerExistsAndNameNotDuplicate()
    {
        // adjust SubHandlerDto constructor to your real record/class
        SubHandlerDto dto = new SubHandlerDto(
                1,
                "Kitchen Cleaning",
                "detail",
                12.2
        );

        Handler handler = Handler.builder()
                .id(1)
                .name("Cleaning")
                .build();

        SubHandler mapped = new SubHandler();
        mapped.setName(dto.name());

        when(handlerService.findHandlerById(dto.handlerId())).thenReturn(handler);
        when(subHandlerService.findSubHandlerByName(dto.name())).thenReturn(null);
        when(entityMapper.dtoToSubHandler(dto)).thenReturn(mapped);

        SubHandler result = adminService.saveSubHandler(dto);

        assertSame(mapped, result);
        assertEquals(handler, result.getHandler());
        verify(subHandlerService).saveSubHandler(mapped);
    }

    @Test
    void saveSubHandler_shouldThrowNotFoundHandler_whenHandlerDoesNotExist()
    {
        SubHandlerDto dto = new SubHandlerDto(
                99,
                "Anything",
                null,
                null
        );

        when(handlerService.findHandlerById(dto.handlerId())).thenReturn(null);

        assertThrows(NotFoundHandler.class,
                () -> adminService.saveSubHandler(dto));

        verify(subHandlerService, never()).saveSubHandler(any());
    }

    @Test
    void saveSubHandler_shouldThrowHandlerIsDuplicate_whenSubHandlerNameExists()
    {
        SubHandlerDto dto = new SubHandlerDto(
                1,
                "Kitchen Cleaning",
                null,
                null
        );

        Handler handler = Handler.builder()
                .id(1)
                .name("Cleaning")
                .build();

        SubHandler existing = new SubHandler();
        existing.setName(dto.name());

        when(handlerService.findHandlerById(dto.handlerId())).thenReturn(handler);
        when(subHandlerService.findSubHandlerByName(dto.name())).thenReturn(existing);

        assertThrows(HandlerIsDuplicate.class,
                () -> adminService.saveSubHandler(dto));

        verify(subHandlerService, never()).saveSubHandler(any());
    }

    // -----------------------------------
    // saveEmployeeToSubHandler()
    // -----------------------------------

    @Test
    void saveEmployeeToSubHandler_shouldThrowNotFoundEmployee_whenEmployeeNotFound()
    {
        when(employeeService.findById(10, Employee.class)).thenReturn(null);

        assertThrows(NotFoundEmployee.class,
                () -> adminService.saveEmployeeToSubHandler(10, 20));
    }

    @Test
    void saveEmployeeToSubHandler_shouldThrowEmployeeIsNotAccepted_whenStateNotAccepted()
    {
        Employee employee = new Employee();
        employee.setId(10);
        employee.setEmployeeState(EmployeeState.UNDER_REVIEW);

        when(employeeService.findById(10, Employee.class)).thenReturn(employee);

        assertThrows(EmployeeIsNotAccepted.class,
                () -> adminService.saveEmployeeToSubHandler(10, 20));

        verify(subHandlerService, never()).findSubHandlerById(any());
        verify(employeeService, never()).updateUser(any());
    }

    @Test
    void saveEmployeeToSubHandler_shouldAddSubHandler_whenEmployeeAccepted()
    {
        Employee employee = new Employee();
        employee.setId(10);
        employee.setEmployeeState(EmployeeState.ACCEPTED);
        employee.setSubHandlers(null);

        SubHandler subHandler = new SubHandler();
        subHandler.setId(20);

        when(employeeService.findById(10, Employee.class)).thenReturn(employee);
        when(subHandlerService.findSubHandlerById(20)).thenReturn(subHandler);

        adminService.saveEmployeeToSubHandler(10, 20);

        assertNotNull(employee.getSubHandlers());
        assertTrue(employee.getSubHandlers().contains(subHandler));
        verify(employeeService).updateUser(employee);
    }

    // -----------------------------------
    // detailPriceSubHandlerChanger()
    // -----------------------------------

    @Test
    void detailPriceSubHandlerChanger_shouldThrowNotFoundSubHandler_whenNotFound()
    {
        ChangeSubHandlerDto dto = new ChangeSubHandlerDto(
                1,
                null,
                null
        );

        when(subHandlerService.findSubHandlerById(dto.id())).thenReturn(null);

        assertThrows(NotFoundSubHandler.class,
                () -> adminService.detailPriceSubHandlerChanger(dto));
    }

    @Test
    void detailPriceSubHandlerChanger_shouldThrowYouInsertNothing_whenNoFieldsProvided()
    {
        SubHandler subHandler = new SubHandler();
        subHandler.setId(1);

        ChangeSubHandlerDto dto = new ChangeSubHandlerDto(
                1,
                null,
                null
        );

        when(subHandlerService.findSubHandlerById(dto.id())).thenReturn(subHandler);

        assertThrows(YouInsertNothing.class,
                () -> adminService.detailPriceSubHandlerChanger(dto));

        verify(subHandlerService, never()).updateSubHandler(any());
    }

    @Test
    void detailPriceSubHandlerChanger_shouldUpdateFields_whenDetailOrPriceProvided()
    {
        SubHandler subHandler = new SubHandler();
        subHandler.setId(1);

        ChangeSubHandlerDto dto = new ChangeSubHandlerDto(
                1,
                "New detail",
                13.2
        );

        when(subHandlerService.findSubHandlerById(dto.id())).thenReturn(subHandler);

        adminService.detailPriceSubHandlerChanger(dto);

        assertEquals("New detail", subHandler.getDetail());
        assertEquals(13.2, subHandler.getBasePrice());
        verify(subHandlerService).updateSubHandler(subHandler);
    }

    // -----------------------------------
    // findCustomerByOptional()
    // -----------------------------------

    @Test
    void findCustomerByOptional_shouldMapEntitiesToOutputs()
    {
        FindFilteredCustomerDto input = mock(FindFilteredCustomerDto.class);

        Customer c1 = new Customer();
        c1.setId(1);
        c1.setName("John");
        c1.setLast_name("Doe");
        c1.setEmail("john@example.com");
        c1.setPhone("123");
        c1.setTimeOfRegistration(LocalDateTime.now());
        c1.setActive(true);

        when(customerService.findCustomerByOptional(input))
                .thenReturn(List.of(c1));

        List<CustomerOutput> result = adminService.findCustomerByOptional(input);

        assertEquals(1, result.size());
        CustomerOutput out = result.get(0);

        assertEquals(c1.getId(), out.id());
        assertEquals(c1.getName(), out.name());
        assertEquals(c1.getLast_name(), out.last_name());
        assertEquals(c1.getEmail(), out.email());
        assertEquals(c1.getPhone(), out.phone());
        assertEquals(c1.getTimeOfRegistration(), out.timeOfRegistration());
        assertEquals(c1.isActive(), out.isActive());
    }

    // -----------------------------------
    // findEmployeesByOptionalInformation()
    // -----------------------------------

    @Test
    void findEmployeesByOptionalInformation_shouldDelegateToEmployeeService()
    {
        EmployeeInputHandlersDto input = mock(EmployeeInputHandlersDto.class);

        List<EmployeeOutputDtoHandlers> list = List.of(mock(EmployeeOutputDtoHandlers.class));

        when(employeeService.findEmployeesByOptionalInformation(input)).thenReturn(list);

        List<EmployeeOutputDtoHandlers> result = adminService.findEmployeesByOptionalInformation(input);

        assertSame(list, result);
    }

    // -----------------------------------
    // findPaidWorksById()
    // -----------------------------------

    @Test
    void findPaidWorksById_shouldDelegateToEmployeeService_whenTypeEmployee()
    {
        List<DoneDutiesDto> list = List.of();

        when(employeeService.findDoneWorksById(5)).thenReturn(list);

        List<DoneDutiesDto> result = adminService.findPaidWorksById(5, TypeOfUser.EMPLOYEE);

        assertSame(list, result);
    }

    @Test
    void findPaidWorksById_shouldDelegateToCustomerService_whenTypeCustomer()
    {
        List<DoneDutiesDto> list = List.of();

        when(customerService.findDoneWorksById(7)).thenReturn(list);

        List<DoneDutiesDto> result = adminService.findPaidWorksById(7, TypeOfUser.CUSTOMER);

        assertSame(list, result);
    }

    @Test
    void findPaidWorksById_shouldThrowFailedDoingOperation_whenTypeUnknown()
    {
        assertThrows(FailedDoingOperation.class,
                () -> adminService.findPaidWorksById(1, null));
    }

    // -----------------------------------
    // optionalFindOrders()
    // -----------------------------------

    @Test
    void optionalFindOrders_shouldMapOrdersToDtos()
    {
        FindFilteredOrdersDto input = mock(FindFilteredOrdersDto.class);

        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("John");
        customer.setLast_name("Doe");

        SubHandler subHandler = new SubHandler();
        subHandler.setName("Cleaning");

        Employee employee = new Employee();
        employee.setId(2);
        employee.setName("Alex");

        Orders order = new Orders();
        order.setOfferedPrice(12.5);
        order.setDetail("detail");
        order.setSubHandler(subHandler);
        order.setTimeOfWork(LocalDateTime.now());
        order.setAddress("Address");
        order.setCustomer(customer);
        order.setEmployee(employee);
        order.setScore(5);
        order.setComment("good");

        when(employeeService.optionalFindOrders(input)).thenReturn(List.of(order));

        List<OrderOutputDto> result = adminService.optionalFindOrders(input);

        assertEquals(1, result.size());

        OrderOutputDto dto = result.get(0);

        assertEquals(12.5, dto.offerPrice());
        assertEquals("detail", dto.detail());
        assertEquals("Cleaning", dto.subHandlerName());
        assertEquals("Address", dto.address());
        assertEquals("John Doe", dto.customerName());
        assertEquals(1, dto.customerId());
        assertEquals("Alex", dto.employeeName());
        assertEquals(2, dto.employeeId());
        assertEquals(5, dto.score());
        assertEquals("good", dto.comment());
    }

    // -----------------------------------
    // findEmployeeByReports()
    // -----------------------------------
    @Test
    void findEmployeeByReports_shouldDelegateToEmployeeService()
    {
        FindFilteredEmployeeDto input = mock(FindFilteredEmployeeDto.class);
        List<EmployeeOutputDtoReport> list = List.of(mock(EmployeeOutputDtoReport.class));

        when(employeeService.findEmployeeByReports(input)).thenReturn(list);

        List<EmployeeOutputDtoReport> result = adminService.findEmployeeByReports(input);

        assertSame(list, result);
    }

    // -----------------------------------
    // removeEmployeeFromSubHandler()
    // -----------------------------------

    @Test
    void removeEmployeeFromSubHandler_shouldCallRepositoryDelete()
    {
        Employee employee = new Employee();
        employee.setId(10);

        SubHandler subHandler = new SubHandler();
        subHandler.setId(20);

        when(employeeService.findById(10, Employee.class)).thenReturn(employee);
        when(subHandlerService.findSubHandlerById(20)).thenReturn(subHandler);

        adminService.removeEmployeeFromSubHandler(10, 20);

        verify(adminRepository).deleteEmployeeFromSubHandler(10, 20);
    }

    // -----------------------------------
    // acceptEmployee()
    // -----------------------------------

    @Test
    void acceptEmployee_shouldSetStateToAccepted_whenIfEmployeeIsAcceptedReturnsTrue()
    {
        Employee employee = new Employee();
        employee.setId(10);
        employee.setEmployeeState(EmployeeState.UNDER_REVIEW);

        when(employeeService.findById(10, Employee.class)).thenReturn(employee);

        adminService.acceptEmployee(10);

        assertEquals(EmployeeState.ACCEPTED, employee.getEmployeeState());
        verify(employeeService).updateUser(employee);
    }

    // -----------------------------------
    // findCustomerByReports()
    // -----------------------------------

    @Test
    void findCustomerByReports_shouldDelegateToCustomerService()
    {
        FindCustomerByFilterDto input = mock(FindCustomerByFilterDto.class);
        List<CustomerOutputDtoForReport> list = List.of(mock(CustomerOutputDtoForReport.class));

        when(customerService.findCustomerByReports(input)).thenReturn(list);

        List<CustomerOutputDtoForReport> result = adminService.findCustomerByReports(input);

        assertSame(list, result);
    }


    @Test
    void saveEmployeeToSubHandler_shouldAddSubHandlerToExistingSet_whenEmployeeAlreadyHasSubHandlers()
    {
        Employee employee = new Employee();
        employee.setId(10);
        employee.setEmployeeState(EmployeeState.ACCEPTED);

        SubHandler existing = new SubHandler();
        existing.setId(1);

        HashSet<SubHandler> currentSet = new HashSet<>();
        currentSet.add(existing);
        employee.setSubHandlers(currentSet);

        SubHandler newSubHandler = new SubHandler();
        newSubHandler.setId(20);

        when(employeeService.findById(10, Employee.class)).thenReturn(employee);
        when(subHandlerService.findSubHandlerById(20)).thenReturn(newSubHandler);

        adminService.saveEmployeeToSubHandler(10, 20);

        assertTrue(employee.getSubHandlers().contains(existing));
        assertTrue(employee.getSubHandlers().contains(newSubHandler));
        verify(employeeService).updateUser(employee);
    }


    @Test
    void saveEmployeeToSubHandler_shouldThrowNotFoundHandler_whenSubHandlerNotFound()
    {
        Employee employee = new Employee();
        employee.setId(10);
        employee.setEmployeeState(EmployeeState.ACCEPTED);

        when(employeeService.findById(10, Employee.class)).thenReturn(employee);
        when(subHandlerService.findSubHandlerById(20)).thenReturn(null);

        assertThrows(NotFoundHandler.class,
                () -> adminService.saveEmployeeToSubHandler(10, 20));

        verify(employeeService, never()).updateUser(any());
    }

    @Test
    void detailPriceSubHandlerChanger_shouldUpdateOnlyDetail_whenBasePriceNull()
    {
        SubHandler subHandler = new SubHandler();
        subHandler.setId(1);

        ChangeSubHandlerDto dto = new ChangeSubHandlerDto(
                1,
                "Only detail",
                null
        );

        when(subHandlerService.findSubHandlerById(dto.id())).thenReturn(subHandler);

        adminService.detailPriceSubHandlerChanger(dto);

        assertEquals("Only detail", subHandler.getDetail());
        assertNull(subHandler.getBasePrice());
        verify(subHandlerService).updateSubHandler(subHandler);
    }

    @Test
    void optionalFindOrders_shouldHandleNullEmployee()
    {
        FindFilteredOrdersDto input = mock(FindFilteredOrdersDto.class);

        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("John");
        customer.setLast_name("Doe");

        SubHandler subHandler = new SubHandler();
        subHandler.setName("Cleaning");

        Orders order = new Orders();
        order.setOfferedPrice(15.0);
        order.setDetail("detail");
        order.setSubHandler(subHandler);
        order.setTimeOfWork(LocalDateTime.now());
        order.setAddress("Address");
        order.setCustomer(customer);
        order.setEmployee(null);
        order.setScore(4);
        order.setComment("ok");

        when(employeeService.optionalFindOrders(input)).thenReturn(List.of(order));

        List<OrderOutputDto> result = adminService.optionalFindOrders(input);

        assertEquals(1, result.size());
        OrderOutputDto dto = result.get(0);

        assertEquals(15.0, dto.offerPrice());
        assertEquals("detail", dto.detail());
        assertEquals("Cleaning", dto.subHandlerName());
        assertEquals("Address", dto.address());
        assertEquals("John Doe", dto.customerName());
        assertEquals(1, dto.customerId());
        assertNull(dto.employeeName());
        assertNull(dto.employeeId());
        assertEquals(4, dto.score());
        assertEquals("ok", dto.comment());
    }

    @Test
    void findPaidWorksById_shouldThrowFailedDoingOperation_whenTypeAdmin()
    {
        assertThrows(FailedDoingOperation.class,
                () -> adminService.findPaidWorksById(1, TypeOfUser.ADMIN));

        verify(employeeService, never()).findDoneWorksById(anyInt());
        verify(customerService, never()).findDoneWorksById(anyInt());
    }

    @Test
    void removeEmployeeFromSubHandler_shouldThrowNotFoundSubHandler_whenSubHandlerNotFound()
    {
        Employee employee = new Employee();
        employee.setId(10);

        when(employeeService.findById(10, Employee.class)).thenReturn(employee);
        when(subHandlerService.findSubHandlerById(20)).thenReturn(null);

        assertThrows(NotFoundSubHandler.class,
                () -> adminService.removeEmployeeFromSubHandler(10, 20));

        verify(adminRepository, never()).deleteEmployeeFromSubHandler(anyInt(), anyInt());
    }

    @Test
    void removeEmployeeFromSubHandler_shouldThrowNotFoundEmployee_whenEmployeeNotFound()
    {
        when(employeeService.findById(10, Employee.class)).thenReturn(null);

        assertThrows(NotFoundEmployee.class,
                () -> adminService.removeEmployeeFromSubHandler(10, 20));

        verify(subHandlerService, never()).findSubHandlerById(anyInt());
        verify(adminRepository, never()).deleteEmployeeFromSubHandler(anyInt(), anyInt());
    }

    @Test
    void acceptEmployee_shouldThrow_whenEmployeeAlreadyAccepted()
    {
        Employee employee = new Employee();
        employee.setId(10);
        employee.setEmployeeState(EmployeeState.ACCEPTED); // already accepted

        when(employeeService.findById(10, Employee.class)).thenReturn(employee);

        assertThrows(RuntimeException.class,
                () -> adminService.acceptEmployee(10));

        // no updateUser should be called if already accepted
        verify(employeeService, never()).updateUser(any());
    }
}

