package org.example.java.org.example.service;
import org.example.domain.*;
import org.example.dto.admin.EmployeeInputHandlersDto;
import org.example.dto.admin.EmployeeOutputDtoHandlers;
import org.example.dto.employee.EmployeeSignUpDto;
import org.example.dto.employee.OfferDto;
import org.example.dto.employee.OrderOutputEmployee;
import org.example.dto.employee.SubHandlerOutput;
import org.example.dto.servisesDone.DoneDutiesDto;
import org.example.dto.user.OrdersOutputDtoUser;
import org.example.enumirations.EmployeeState;
import org.example.enumirations.OrderState;
import org.example.enumirations.TypeOfUser;
import org.example.events.UserCreationEvent;
import org.example.exeptions.NotFoundException.NotFoundEmployee;
import org.example.exeptions.NotFoundException.NotFoundOffer;
import org.example.exeptions.duplicate.DuplicateException;
import org.example.exeptions.emailToken.InvalidTokenExceptions;
import org.example.exeptions.employee.ImageIsTooBig;
import org.example.exeptions.employee.NotJpgFile;
import org.example.exeptions.order.OfferPriceIsLessThanOrderPrice;
import org.example.exeptions.order.OrderStateIsNotCorrect;
import org.example.exeptions.password.AllNotBeLetterOrDigits;
import org.example.exeptions.password.PassNot8Digits;
import org.example.exeptions.wrongTime.TimeOfWorkDoesntMatch;
import org.example.repository.user.employee.EmployeeRepository;
import org.example.service.credit.CreditService;
import org.example.service.emailToken.EmailTokenService;
import org.example.service.mapStruct.EntityMapper;
import org.example.service.offer.OfferService;
import org.example.service.order.OrderService;
import org.example.service.subHandler.SubHandlerService;
import org.example.service.user.employee.imp.EmployeeServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImpTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ApplicationEventPublisher publisher;

    @Mock
    private EntityMapper entityManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private OrderService orderService;

    @Mock
    private OfferService offerService;

    @Mock
    private SubHandlerService subHandlerService;

    @Mock
    private EmailTokenService emailTokenService;

    @Mock
    private CreditService creditService;
    @InjectMocks
    private EmployeeServiceImp employeeService;

    @Test
    void signUpEmployee_validData_shouldCreateEmployeePublishEventAndSendToken()
    {
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
        EmployeeSignUpDto result = employeeService.signUpEmployee(dto,imageFile);

        assertSame(dto, result);
        verify(publisher).publishEvent(any(UserCreationEvent.class));
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void signUpEmployee_invalidData_shouldThrowPasswordException()
    {
        EmployeeSignUpDto dto = new EmployeeSignUpDto(
                "John",
                "Doe",
                "john@example.com",
                "09023335595",
                "123456a"
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


        assertThrows(
                PassNot8Digits.class,
                () -> employeeService.signUpEmployee(dto, imageFile)
        );
    }

    @Test
    void signUpEmployee_invalidData_shouldThrowPasswordDoesNotHaveLetter()
    {
        EmployeeSignUpDto dto = new EmployeeSignUpDto(
                "John",
                "Doe",
                "john@example.com",
                "09023335595",
                "12345612"
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


        assertThrows(
                AllNotBeLetterOrDigits.class,
                () -> employeeService.signUpEmployee(dto, imageFile)
        );
    }


    @Test
    void checkIfNotDuplicateUser_shouldDoNothing_whenUserNotFound()
    {
        String phone = "09120000000";

        when(employeeRepository.find(phone, Employee.class))
                .thenReturn(null);

        assertDoesNotThrow(
                () -> employeeService.checkIfNotDuplicateUser(phone)
        );

        verify(employeeRepository, times(1))
                .find(phone, Employee.class);
    }

    @Test
    void checkIfNotDuplicateUser_shouldThrowDuplicateException_whenUserExists()
    {
        String phone = "09120000000";

        Employee existing = new Employee();
        existing.setId(1);

        when(employeeRepository.find(phone, Employee.class))
                .thenReturn(existing);

        assertThrows(
                DuplicateException.class,
                () -> employeeService.checkIfNotDuplicateUser(phone)
        );

        verify(employeeRepository, times(1))
                .find(phone, Employee.class);
    }

    @Test
    void signUpEmployee_validData_shouldThrowBigImageExceptionWhenImageIsMoreThan3Mb()
    {
        EmployeeSignUpDto dto = new EmployeeSignUpDto(
                "John",
                "Doe",
                "john@example.com",
                "09023335595",
                "123456za"
        );
        byte[] big = new byte[300 * 1024];
        big[0] = (byte) 0xFF;
        big[1] = (byte) 0xD8;
        big[big.length - 2] = (byte) 0xFF;
        big[big.length - 1] = (byte) 0xD9;

        MultipartFile bigFile = new MockMultipartFile(
                "image",
                "big.jpg",
                "image/jpeg",
                big
        );
        assertThrows(
                ImageIsTooBig.class,
                () -> employeeService.signUpEmployee(dto, bigFile)
        );
    }

    @Test
    void signUpEmployee_invalidImage_shouldThrowNotJpgFile_whenFileTooShort()
    {
        EmployeeSignUpDto dto = new EmployeeSignUpDto(
                "John",
                "Doe",
                "john@example.com",
                "09023335595",
                "123456za"
        );

        // length = 3  ->  imageBytes.length < 4
        byte[] tooShort = new byte[] { 0x01, 0x02, 0x03 };

        MultipartFile file = new MockMultipartFile(
                "image",
                "short.jpg",
                "image/jpeg",
                tooShort
        );

        assertThrows(
                NotJpgFile.class,
                () -> employeeService.signUpEmployee(dto, file)
        );
    }

    @Test
    void signUpEmployee_invalidImage_shouldThrowNotJpgFile_whenHeaderIsNotJpg()
    {
        EmployeeSignUpDto dto = new EmployeeSignUpDto(
                "John",
                "Doe",
                "john@example.com",
                "09023335595",
                "123456za"
        );

        byte[] invalidHeader = new byte[10];
        // wrong header
        invalidHeader[0] = 0x00;
        invalidHeader[1] = 0x11;
        // valid footer so we only test header
        invalidHeader[8] = (byte) 0xFF;
        invalidHeader[9] = (byte) 0xD9;

        MultipartFile file = new MockMultipartFile(
                "image",
                "bad-header.jpg",
                "image/jpeg",
                invalidHeader
        );

        assertThrows(
                NotJpgFile.class,
                () -> employeeService.signUpEmployee(dto, file)
        );
    }

    @Test
    void signUpEmployee_invalidImage_shouldThrowNotJpgFile_whenFooterIsNotJpg()
    {
        EmployeeSignUpDto dto = new EmployeeSignUpDto(
                "John",
                "Doe",
                "john@example.com",
                "09023335595",
                "123456za"
        );

        byte[] invalidFooter = new byte[10];
        invalidFooter[0] = (byte) 0xFF;
        invalidFooter[1] = (byte) 0xD8;
        invalidFooter[8] = 0x00;
        invalidFooter[9] = 0x11;

        MultipartFile file = new MockMultipartFile(
                "image",
                "bad-footer.jpg",
                "image/jpeg",
                invalidFooter
        );

        assertThrows(
                NotJpgFile.class,
                () -> employeeService.signUpEmployee(dto, file)
        );
    }

    private OfferDto randomOfferDto()
    {
        long offerPrice = 50_000L;
        LocalDateTime timeOfWork = LocalDateTime.now().plusDays(1);
        int workTimeInMinutes = 120;
        int orderId = 10;
        int employeeId = 20;

        return new OfferDto(
                offerPrice,
                timeOfWork,
                workTimeInMinutes,
                orderId,
                employeeId
        );
    }
    //give offer to order test
    @Test
    void giveOfferToOrderTest(){
        OfferDto offerDto = randomOfferDto();
        when(employeeRepository.employeeExistsById(offerDto.employeeId())).thenReturn(true);
        Orders orders = new Orders();
        orders.setId(offerDto.orderId());
        orders.setOrderState(OrderState.UNDER_CHOOSING_EMPLOYEE);
        orders.setTimeOfWork(offerDto.timeOfWork().minusDays(1));
        orders.setOfferedPrice(offerDto.offerPrice()-100d);
        when(orderService.findById(offerDto.orderId())).thenReturn(orders);
        Offer offer = new Offer();
        when(entityManager.dtoToOffer(offerDto))
                .thenReturn(offer);
        assertDoesNotThrow(() -> employeeService.giveOfferToOrder(offerDto));
    }


    @Test
    void findAllSubHandlerForEmployeeTest(){
        List<SubHandler> foundSubhandlers = List.of(SubHandler.builder().id(1)
                .name("caring")
                        .detail("caring detail")
                .basePrice(12.2)
                .build());
        Integer employeeId;
        employeeId = 1;
        when(subHandlerService.subHandlersForEmployee(employeeId)).thenReturn(foundSubhandlers);
        SubHandlerOutput subHandlerOutput = new SubHandlerOutput(employeeId,"caring"
                ,"caring detail",12.2);
        when(entityManager.subHandlerToDto(foundSubhandlers.get(0))).thenReturn(subHandlerOutput);
        List<SubHandlerOutput> result = employeeService.findAllSubHandlersForEmployee(employeeId);
        assertEquals(1, result.size());
        assertEquals(subHandlerOutput, result.get(0));
    }


    @Test
    void findAllSubHandlerForEmployeeEmptyListTest(){
        Integer employeeId;
        employeeId = 1;
        when(subHandlerService.subHandlersForEmployee(employeeId))
                .thenReturn(List.of());
        List<SubHandlerOutput> result =
                employeeService.findAllSubHandlersForEmployee(employeeId);
        assertTrue(result.isEmpty());
        verify(entityManager, never()).subHandlerToDto(any());
    }

    @Test
    void findAllSubHandlerForEmployee_multipleSubHandlers_returnsMappedDtosInOrder()
    {
        Integer employeeId = 1;

        SubHandler s1 = SubHandler.builder()
                .id(1)
                .name("caring")
                .detail("caring detail")
                .basePrice(12.2)
                .build();

        SubHandler s2 = SubHandler.builder()
                .id(2)
                .name("cleaning")
                .detail("cleaning detail")
                .basePrice(20.0)
                .build();

        List<SubHandler> found = List.of(s1, s2);

        when(subHandlerService.subHandlersForEmployee(employeeId))
                .thenReturn(found);
        SubHandlerOutput dto1 = new SubHandlerOutput(1, "caring", "caring detail", 12.2);
        SubHandlerOutput dto2 = new SubHandlerOutput(2, "cleaning", "cleaning detail", 20.0);
        when(entityManager.subHandlerToDto(s1)).thenReturn(dto1);
        when(entityManager.subHandlerToDto(s2)).thenReturn(dto2);
        List<SubHandlerOutput> result =
                employeeService.findAllSubHandlersForEmployee(employeeId);
        assertEquals(2, result.size());
        assertSame(dto1, result.get(0));
        assertSame(dto2, result.get(1));
        verify(subHandlerService).subHandlersForEmployee(employeeId);
        verify(entityManager, times(1)).subHandlerToDto(s1);
        verify(entityManager, times(1)).subHandlerToDto(s2);
        verifyNoMoreInteractions(entityManager, subHandlerService);
    }

    @Test
    void findAllSubHandlersForEmployee_whenEmpty_shouldReturnEmptyAndNotCallMapper()
    {
        Integer employeeId = 1;
        when(subHandlerService.subHandlersForEmployee(employeeId))
                .thenReturn(List.of());
        List<SubHandlerOutput> result = employeeService.findAllSubHandlersForEmployee(employeeId);
        assertEquals(0, result.size());
        verify(subHandlerService, times(1)).subHandlersForEmployee(employeeId);
        verify(entityManager, never()).subHandlerToDto(any(SubHandler.class));
        verifyNoMoreInteractions(subHandlerService, entityManager);
    }


    @Test
    void findEmployeeByOptionalInformation_happyPath()
    {
        EmployeeInputHandlersDto input = optionalFilterTestInputWithMinAndMaxScore();

        when(employeeRepository.selectEmployeesByOptionalInformation(input))
                .thenReturn(randomEmployees());


        List<EmployeeOutputDtoHandlers> result =
                employeeService.findEmployeesByOptionalInformation(input);


        assertNotNull(result);
        assertEquals(3, result.size());


        EmployeeOutputDtoHandlers r1 = result.get(0);
        assertEquals(1, r1.id());
        assertEquals("Ali", r1.name());
        assertEquals("Masoumi", r1.lastName());
        assertEquals("ali1@example.com", r1.email());
        assertEquals("09120000001", r1.phone());
        assertEquals(EmployeeState.NEW, r1.employeeState());
        assertEquals(10, r1.score());

        assertNotNull(r1.subHandlersName());
        assertEquals(1, r1.subHandlersName().size());
        assertTrue(r1.subHandlersName().get(0).contains("name : caring"));


        verify(employeeRepository, times(1)).selectEmployeesByOptionalInformation(input);
        verifyNoMoreInteractions(employeeRepository);
    }

    public static List<Employee> randomEmployees()
    {
        Employee e1 =
                Employee.builder()
                        .id(1)
                        .name("Ali")
                        .last_name("Masoumi")
                        .email("ali1@example.com")
                        .phone("09120000001")
                        .employeeState(EmployeeState.NEW)
                        .score(10)
                        .image(new byte[] { 1, 2, 3 })
                        .subHandlers(Set.of(subHandler(1, "caring", "caring detail", 12.2)))
                        .credit(credit(0.0))
                        .build();

        Employee e2 =
                Employee.builder()
                        .id(2)
                        .name("Reza")
                        .last_name("Ahmadi")
                        .email("reza2@example.com")
                        .phone("09120000002")
                        .employeeState(EmployeeState.UNDER_REVIEW)
                        .score(55)
                        .image(new byte[] { 9, 8, 7 })
                        .subHandlers(Set.of(
                                subHandler(2, "plumbing", "plumbing detail", 30.0),
                                subHandler(3, "electric", "electric detail", 40.0)
                        ))
                        .credit(credit(120.0))
                        .build();

        Employee e3 =
                Employee.builder()
                        .id(3)
                        .name("Sara")
                        .last_name("Karimi")
                        .email("sara3@example.com")
                        .phone("09120000003")
                        .employeeState(EmployeeState.ACCEPTED)
                        .score(90)
                        .image(new byte[] { 5, 5, 5 })
                        .subHandlers(new HashSet<>())
                        .credit(new Credit(12.2,TypeOfUser.EMPLOYEE))
                        .build();

        return List.of(e1, e2, e3);
    }

    private static SubHandler subHandler(Integer id, String name, String detail, Double basePrice)
    {
        return SubHandler.builder()
                .id(id)
                .name(name)
                .detail(detail)
                .basePrice(basePrice)
                .handler(Handler.builder().id(1).name("important").build())
                .build();
    }

    private static Credit credit(Double amount)
    {
        return Credit.builder()
                .typeOfUser(TypeOfUser.EMPLOYEE)
                .amount(amount)
                .build();
    }

    public static EmployeeInputHandlersDto optionalFilterTestInputWithMinAndMaxScore()
    {
        return new EmployeeInputHandlersDto(
                null,
                null,
                null,
                null,
                null,
                null,
                4,
                100,
                true
        );
    }

    @Test
    void findEmployeeByOptionalInformation_whenRepoReturnsEmptyList_returnsEmptyList()
    {
        EmployeeInputHandlersDto input = optionalFilterTestInputWithMinAndMaxScore();

        when(employeeRepository.selectEmployeesByOptionalInformation(input))
                .thenReturn(Collections.emptyList());

        List<EmployeeOutputDtoHandlers> result =
                employeeService.findEmployeesByOptionalInformation(input);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(employeeRepository, times(1)).selectEmployeesByOptionalInformation(input);
    }

    @Test
    void findEmployeesByOptionalInformation_whenRepoReturnsNull_returnsEmptyList()
    {
        EmployeeInputHandlersDto input = optionalFilterTestInputWithMinAndMaxScore();

        when(employeeRepository.selectEmployeesByOptionalInformation(input))
                .thenReturn(null);

        List<EmployeeOutputDtoHandlers> result =
                employeeService.findEmployeesByOptionalInformation(input);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findEmployeesByOptionalInformation_whenEmployeeSubHandlersIsNull_addsEmptySubHandlerList()
    {
        EmployeeInputHandlersDto input = optionalFilterTestInputWithMinAndMaxScore();

        Employee e =
                Employee.builder()
                        .id(1)
                        .name("Ali")
                        .last_name("Masoumi")
                        .email("ali1@example.com")
                        .phone("09120000001")
                        .employeeState(EmployeeState.NEW)
                        .score(10)
                        .subHandlers(null)
                        .build();

        when(employeeRepository.selectEmployeesByOptionalInformation(input))
                .thenReturn(List.of(e));

        List<EmployeeOutputDtoHandlers> result =
                employeeService.findEmployeesByOptionalInformation(input);

        assertEquals(1, result.size());
        assertTrue(result.get(0).subHandlersName().isEmpty()); // adjust getter name if different
    }

    @Test
    void findOrdersForEmployeeTest(){
        Integer employeeId = 1;

        SubHandler sh1 = new SubHandler();
        sh1.setId(10);
        sh1.setName("caring");

        when(subHandlerService.subHandlersForEmployee(employeeId))
                .thenReturn(List.of(sh1));

        Customer customer = new Customer();
        customer.setId(100);
        customer.setName("Ali");

        Orders o1 = new Orders();
        o1.setId(1);
        o1.setOfferedPrice(50.0);
        o1.setDetail("d1");
        o1.setSubHandler(sh1);
        o1.setTimeOfWork(LocalDateTime.now().plusDays(1));
        o1.setAddress("addr1");
        o1.setOrderState(OrderState.UNDER_CHOOSING_EMPLOYEE);
        o1.setCustomer(customer);

        Orders o2 = new Orders();
        o2.setId(2);
        o2.setOfferedPrice(60.0);
        o2.setDetail("d2");
        o2.setSubHandler(sh1);
        o2.setTimeOfWork(LocalDateTime.now().plusDays(2));
        o2.setAddress("addr2");
        o2.setOrderState(OrderState.WAITING_FOR_EMPLOYEE_OFFER);
        o2.setCustomer(customer);

        Orders o3 = new Orders();
        o3.setId(3);
        o3.setOrderState(OrderState.PAID);

        when(orderService.findOrdersForSubHandler(10))
                .thenReturn(List.of(o1, o2, o3));

        List<OrderOutputEmployee> result = employeeService.findOrdersForEmployee(employeeId);

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).id());
        assertEquals("caring", result.get(0).subHandlerName());
        assertEquals("Ali", result.get(0).customerName());
        assertEquals(100, result.get(0).customerId());

        verify(subHandlerService).subHandlersForEmployee(employeeId);
        verify(orderService).findOrdersForSubHandler(10);
    }

    @Test
    void findOrdersForEmployee_whenOrdersPerSubHandlerEmpty_returnsEmpty()
    {
        Integer employeeId = 1;

        SubHandler sh1 = new SubHandler();
        sh1.setId(10);

        when(subHandlerService.subHandlersForEmployee(employeeId))
                .thenReturn(List.of(sh1));

        when(orderService.findOrdersForSubHandler(10))
                .thenReturn(List.of());

        List<OrderOutputEmployee> result = employeeService.findOrdersForEmployee(employeeId);

        assertTrue(result.isEmpty());
        verify(subHandlerService).subHandlersForEmployee(employeeId);
        verify(orderService).findOrdersForSubHandler(10);
    }

    @Test
    void findOrdersForEmployee_whenSubHandlersEmpty_returnsEmpty()
    {
        Integer employeeId = 1;

        when(subHandlerService.subHandlersForEmployee(employeeId))
                .thenReturn(List.of());

        List<OrderOutputEmployee> result = employeeService.findOrdersForEmployee(employeeId);

        assertTrue(result.isEmpty());
        verify(subHandlerService).subHandlersForEmployee(employeeId);
        verifyNoInteractions(orderService);
    }

    @Test
    void findOrdersForEmployee_whenSubHandlersIsNull_returnsEmpty()
    {
        Integer employeeId = 1;

        when(subHandlerService.subHandlersForEmployee(employeeId))
                .thenReturn(null);

        List<OrderOutputEmployee> result = employeeService.findOrdersForEmployee(employeeId);

        assertTrue(result.isEmpty());
        verify(subHandlerService).subHandlersForEmployee(employeeId);
        verifyNoInteractions(orderService);
    }


    public static List<Orders> ordersForOptionalSelect()
    {
        SubHandler subHandler = SubHandler.builder().id(1).name("Pairing").detail("ParingThing").build();
        Employee employee = Employee.builder().id(1).name("Dave").last_name("Robinson").subHandlers(
                Set.of(subHandler)
        ).employeeState(EmployeeState.ACCEPTED).build();

        Customer customer = Customer.builder().id(1).name("Customer dave").last_name("Robinson").phone("12345262").isActive(true).build();

        Orders o1 = new Orders();
        o1.setId(10);
        o1.setOfferedPrice(120.0);
        o1.setDetail("Fix sink leakage");
        o1.setTimeOfWork(LocalDateTime.now().plusDays(2));
        o1.setAddress("Berlin, Alexanderplatz");
        o1.setOrderState(OrderState.WAITING_FOR_EMPLOYEE_OFFER);
        o1.setScore(0);
        o1.setComment(null);
        o1.setEmployee(employee);
        o1.setCustomer(customer);
        o1.setSubHandler(subHandler);

        Orders o2 = new Orders();
        o2.setId(11);
        o2.setOfferedPrice(300.0);
        o2.setDetail("Install electric outlet");
        o2.setTimeOfWork(LocalDateTime.now().plusDays(3));
        o2.setAddress("Berlin, Mitte");
        o2.setOrderState(OrderState.UNDER_CHOOSING_EMPLOYEE);
        o2.setScore(5);
        o2.setComment("Customer prefers afternoon");
        o2.setEmployee(employee);
        o2.setCustomer(customer);
        o2.setSubHandler(subHandler);

        Orders o3 = new Orders();
        o3.setId(12);
        o3.setOfferedPrice(80.0);
        o3.setDetail("General cleaning");
        o3.setTimeOfWork(LocalDateTime.now().plusDays(1));
        o3.setAddress("Berlin, Charlottenburg");
        o3.setOrderState(OrderState.PAID);
        o3.setScore(10);
        o3.setComment("Great work");
        o3.setEmployee(employee);
        o3.setCustomer(customer);
        o3.setSubHandler(subHandler);
        o3.setComment("it is paid order well done !");

        return List.of(o1, o2, o3);
    }


    @Test
    void optionalSelectOrdersForEmployeeTest()
    {
        Integer employeeId = 1;
        OrderState orderState = OrderState.PAID;

        List<Orders> orders = ordersForOptionalSelect();

        when(orderService.optionalFindOrdersForEmployee(employeeId, orderState.name()))
                .thenReturn(List.of(orders.get(2)));

        List<OrdersOutputDtoUser> result =
                employeeService.optionalSelectOrdersForEmployee(employeeId, orderState.name());

        assertEquals(1, result.size());
        assertEquals(OrderState.PAID, result.get(0).orderState());

        verify(orderService).optionalFindOrdersForEmployee(employeeId, orderState.name());
        verifyNoMoreInteractions(orderService);
    }

    //getCreditAmount
    @Test
    void getCreditAmountTest(){
        int employeeId = 1;
        Double amount = 12.2;
        Credit credit = new Credit();
        credit.setAmount(amount);
        credit.setTypeOfUser(TypeOfUser.EMPLOYEE);
        when(creditService.findByEmployeeId(employeeId)).thenReturn(credit);
        Double result = employeeService.getCreditAmount(employeeId);
        assertEquals(12.2, result);
    }

    @Test
    void getCreditAmountNullTest(){
        int employeeId = 1;
        when(creditService.findByEmployeeId(employeeId)).thenReturn(null);
        assertThrows(NotFoundEmployee.class, () -> employeeService.getCreditAmount(employeeId));
    }

    //findDoneWorksById
    @Test
    void findDoneWorkByIdTest(){
        Integer employeeId = 1;
        List<Orders> foundedOrder = ordersForOptionalSelect();
        when(orderService.findPaidOrdersForEmployee(employeeId)).thenReturn(foundedOrder);
        for(Orders o : foundedOrder){
            if (o.getOrderState() == OrderState.PAID) {
                Offer offer = creatingTheOffer(o, foundedOrder, employeeId);
                when(offerService.findAcceptedOfferInOrder(o.getId())).thenReturn(offer);
            }
        }
        List<DoneDutiesDto> result = employeeService.findDoneWorksById(employeeId);
        assertEquals(1, result.size());
        assertEquals(result.get(0).comment(),foundedOrder.get(2).getComment());
    }

    @Test
    void findDoneWorkWithNullOffer(){
        Integer employeeId = 1;
        when(orderService.findPaidOrdersForEmployee(employeeId)).thenReturn(null);
        List<DoneDutiesDto> result = employeeService.findDoneWorksById(employeeId);
        assertTrue(result.isEmpty());
    }

    @Test
    void findDoneWorkWithEmptyOffer(){
        Integer employeeId = 1;
        when(orderService.findPaidOrdersForEmployee(employeeId)).thenReturn(List.of());
        List<DoneDutiesDto> result = employeeService.findDoneWorksById(employeeId);
        assertTrue(result.isEmpty());
    }
    private static Offer creatingTheOffer(Orders o, List<Orders> foundedOrder, Integer employeeId) {
        Offer offer = new Offer();
        offer.setId(1);
        offer.setOrders(foundedOrder.get(1));
        offer.setEmployee(Employee.builder().id(employeeId).build());
        offer.setOfferPrice((long) (o.getOfferedPrice() + 100L));
        return offer;
    }

    @Test
    void findDoneWorksById_shouldThrow_whenPaidOrderHasNoOffer() {
        Integer employeeId = 1;
        Orders paid = ordersForOptionalSelect().get(2); // PAID
        when(orderService.findPaidOrdersForEmployee(employeeId)).thenReturn(List.of(paid));
        when(offerService.findAcceptedOfferInOrder(paid.getId())).thenReturn(null);

        assertThrows(NotFoundOffer.class,
                () -> employeeService.findDoneWorksById(employeeId));
    }

    @Test
    void validateEmployeeEmail_shouldThrow_whenTokenNotFound()
    {
        String token = "tok-1";
        when(emailTokenService.findByToken(token)).thenReturn(null);

        assertThrows(InvalidTokenExceptions.class,
                () -> employeeService.validateEmployeeEmail(token));

        verify(emailTokenService, never()).validateToken(anyString());
        verify(employeeRepository, never()).SetUnderReviewState(anyString());
    }

    @Test
    void validateEmployeeEmail_shouldSetUnderReviewAndExpireToken_whenEmployeeExists()
    {
        String token = "tok-2";
        String email = "ali@example.com";

        EmailToken emailToken = new EmailToken();
        emailToken.setEmail(email);
        emailToken.setExpired(false);

        when(emailTokenService.findByToken(token)).thenReturn(emailToken);
        when(employeeRepository.employeeExistsByEmail(email)).thenReturn(true);

        String result = employeeService.validateEmployeeEmail(token);

        assertEquals("successful", result);
        verify(emailTokenService).validateToken(token);
        verify(employeeRepository).SetUnderReviewState(email);
        assertTrue(emailToken.isExpired());
    }

    @Test
    void validateEmployeeEmail_shouldNotChangeState_whenEmployeeDoesNotExist()
    {
        String token = "tok-3";
        String email = "noone@example.com";

        EmailToken emailToken = new EmailToken();
        emailToken.setEmail(email);
        emailToken.setExpired(false);

        when(emailTokenService.findByToken(token)).thenReturn(emailToken);
        when(employeeRepository.employeeExistsByEmail(email)).thenReturn(false);

        String result = employeeService.validateEmployeeEmail(token);

        assertEquals("successful", result);
        verify(emailTokenService).validateToken(token);
        verify(employeeRepository, never()).SetUnderReviewState(anyString());
        assertFalse(emailToken.isExpired());
    }

    @Test
    void giveOfferToOrder_shouldThrowOrderStateIsNotCorrect_whenOrderStateIsInvalid()
    {
        OfferDto offerDto = randomOfferDto();

        when(employeeRepository.employeeExistsById(offerDto.employeeId()))
                .thenReturn(true);

        Orders orders = new Orders();
        orders.setId(offerDto.orderId());
        orders.setOrderState(OrderState.PAID);
        orders.setTimeOfWork(offerDto.timeOfWork().minusDays(1));
        orders.setOfferedPrice(offerDto.offerPrice() - 100d);

        when(orderService.findById(offerDto.orderId()))
                .thenReturn(orders);

        assertThrows(OrderStateIsNotCorrect.class,
                () -> employeeService.giveOfferToOrder(offerDto));

        verify(entityManager, never()).dtoToOffer(any());
        verify(orderService, never()).update(any());
        verify(offerService, never()).save(any());
    }

    @Test
    void giveOfferToOrder_shouldThrowTimeOfWorkDoesntMatch_whenOfferTimeIsNotAfterOrderTime()
    {
        OfferDto offerDto = randomOfferDto();

        when(employeeRepository.employeeExistsById(offerDto.employeeId()))
                .thenReturn(true);

        Orders orders = new Orders();
        orders.setId(offerDto.orderId());
        orders.setOrderState(OrderState.WAITING_FOR_EMPLOYEE_OFFER);
        orders.setTimeOfWork(offerDto.timeOfWork().plusHours(2));
        orders.setOfferedPrice(offerDto.offerPrice() - 100d);

        when(orderService.findById(offerDto.orderId()))
                .thenReturn(orders);

        assertThrows(TimeOfWorkDoesntMatch.class,
                () -> employeeService.giveOfferToOrder(offerDto));

        verify(entityManager, never()).dtoToOffer(any());
        verify(orderService, never()).update(any());
        verify(offerService, never()).save(any());
    }
    @Test
    void giveOfferToOrder_notFoundEmployeeException(){
        OfferDto offerDto = randomOfferDto();
        when(employeeRepository.employeeExistsById(offerDto.employeeId())).thenReturn(false);
        assertThrows(NotFoundEmployee.class,
                () -> employeeService.giveOfferToOrder(offerDto));
        verify(entityManager, never()).dtoToOffer(any());
        verify(orderService, never()).update(any());
        verify(offerService, never()).save(any());
    }

    @Test
    void giveOfferToOrder_shouldThrowOfferPriceIsLessThanOrderPrice_whenOfferPriceIsLower()
    {
        OfferDto offerDto = randomOfferDto();

        when(employeeRepository.employeeExistsById(offerDto.employeeId()))
                .thenReturn(true);

        Orders orders = new Orders();
        orders.setId(offerDto.orderId());
        orders.setOrderState(OrderState.WAITING_FOR_EMPLOYEE_OFFER);
        orders.setTimeOfWork(offerDto.timeOfWork().minusHours(2));
        orders.setOfferedPrice(offerDto.offerPrice() + 1000d);

        when(orderService.findById(offerDto.orderId()))
                .thenReturn(orders);

        assertThrows(OfferPriceIsLessThanOrderPrice.class,
                () -> employeeService.giveOfferToOrder(offerDto));

        verify(entityManager, never()).dtoToOffer(any());
        verify(orderService, never()).update(any());
        verify(offerService, never()).save(any());
    }

}
