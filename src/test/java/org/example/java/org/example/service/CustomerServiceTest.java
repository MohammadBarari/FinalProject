package org.example.java.org.example.service;

import org.example.domain.*;
import org.example.dto.admin.CustomerOutputDtoForReport;
import org.example.dto.admin.FindCustomerByFilterDto;
import org.example.dto.admin.FindFilteredCustomerDto;
import org.example.dto.customer.*;
import org.example.dto.orders.OrderDto;
import org.example.dto.servisesDone.DoneDutiesDto;
import org.example.dto.subHandlers.SubHandlersDtoOutputId;
import org.example.dto.user.OrdersOutputDtoUser;
import org.example.enumirations.OrderState;
import org.example.enumirations.TypeOfUser;
import org.example.events.UserCreationEvent;
import org.example.exeptions.NotFoundException.NotFoundCustomer;
import org.example.exeptions.NotFoundException.NotFoundHandler;
import org.example.exeptions.NotFoundException.NotFoundOffer;
import org.example.exeptions.NotFoundException.NotFoundOrder;
import org.example.exeptions.customer.InvalidCustomerDataException;
import org.example.exeptions.duplicate.DuplicateCommentException;
import org.example.exeptions.emailToken.InvalidTokenExceptions;
import org.example.exeptions.money.DontHaveEnoughMoney;
import org.example.exeptions.order.OrderPriceShouldBeHigherThanBase;
import org.example.exeptions.order.OrderStateIsNotCorrect;
import org.example.exeptions.order.StarShouldBeenBetween1To5;
import org.example.exeptions.password.AllNotBeLetterOrDigits;
import org.example.exeptions.password.PassNot8Digits;
import org.example.exeptions.wrongTime.TimeOfWorkDoesntMatch;
import org.example.repository.passAndUser.PassAndUserRepository;
import org.example.repository.user.BaseUserRepository;
import org.example.repository.user.customer.CustomerRepository;
import org.example.service.credit.CreditService;
import org.example.service.customerCart.CustomerCartService;
import org.example.service.emailToken.EmailTokenService;
import org.example.service.handler.HandlerService;
import org.example.service.mainService.combinedClassesService.CombinedUserClassFromCustomer;
import org.example.service.mapStruct.EntityMapper;
import org.example.service.offer.OfferService;
import org.example.service.order.OrderService;
import org.example.service.subHandler.SubHandlerService;
import org.example.service.user.customer.imp.CustomerServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @Mock
    private BaseUserRepository<Customer> baseUserRepository;

    @Mock
    private CreditService creditService;

    @Mock
    private EmailTokenService emailTokenService;

    @Mock
    private EntityMapper entityMapper;

    @Mock
    private CombinedUserClassFromCustomer combineUserClass;

    @Mock
    private CustomerCartService customerCartService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private SubHandlerService subHandlerService;

    @Mock
    private OrderService orderService;

    @Mock
    private OfferService offerService;

    @Mock
    private HandlerService handlerService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PassAndUserRepository passAndUserRepository;

    @Mock
    private ApplicationEventPublisher publisher;
    private CustomerServiceImp customerService;

    @BeforeEach
    void setUp()
    {
         customerService = new CustomerServiceImp(
                baseUserRepository,
                creditService,
                emailTokenService,
                entityMapper,
                combineUserClass,
                customerCartService,
                customerRepository,
                subHandlerService,
                orderService,
                offerService,
                handlerService,
                passwordEncoder,
                passAndUserRepository,
                publisher
        );
    }

    @Test
    void validateCustomerCanPay_shouldReturnTrue_whenOrderBelongsToCustomerAndEnoughCreditAndDoneState()
    {
        Customer customer = new Customer();
        customer.setId(1);

        Credit credit = new Credit();
        credit.setId(100);
        credit.setAmount(200.0);
        customer.setCredit(credit);

        Orders order = new Orders();
        order.setId(10);
        order.setCustomer(customer);
        order.setOrderState(OrderState.DONE);

        Offer offer = new Offer();
        offer.setOfferPrice(150L);

        boolean result = customerService.validateCustomerCanPay(order, customer, offer);

        assertTrue(result);
    }

    @Test
    void validateCustomerCanPay_shouldThrowDontHaveEnoughMoney_whenCreditIsNotEnough()
    {
        Customer customer = new Customer();
        customer.setId(1);

        Credit credit = new Credit();
        credit.setId(100);
        credit.setAmount(50.0);
        customer.setCredit(credit);

        Orders order = new Orders();
        order.setId(10);
        order.setCustomer(customer);
        order.setOrderState(OrderState.DONE);

        Offer offer = new Offer();
        offer.setOfferPrice(150L);

        assertThrows(
                DontHaveEnoughMoney.class,
                () -> customerService.validateCustomerCanPay(order, customer, offer)
        );
    }

    @Test
    void validateCustomerCanPay_shouldThrowNotFoundCustomer_whenOrderDoesNotBelongToCustomer()
    {
        Customer customer = new Customer();
        customer.setId(1);

        Credit credit = new Credit();
        credit.setId(100);
        credit.setAmount(200.0);
        customer.setCredit(credit);

        Customer anotherCustomer = new Customer();
        anotherCustomer.setId(2);

        Orders order = new Orders();
        order.setId(10);
        order.setCustomer(anotherCustomer);
        order.setOrderState(OrderState.DONE);

        Offer offer = new Offer();
        offer.setOfferPrice(150L);

        assertThrows(
                NotFoundCustomer.class,
                () -> customerService.validateCustomerCanPay(order, customer, offer)
        );
    }


    @Test
    void customerPay_shouldTransferMoneyAndMarkOrderPaid_whenDataIsValid()
    {
        Integer ordersId = 10;
        int customerId = 1;

        Customer customer = new Customer();
        customer.setId(customerId);

        Credit customerCredit = new Credit();
        customerCredit.setId(100);
        customerCredit.setAmount(500.0);
        customer.setCredit(customerCredit);

        Orders order = new Orders();
        order.setId(ordersId);
        order.setCustomer(customer);
        order.setOrderState(OrderState.DONE);

        org.example.domain.Employee employee = new org.example.domain.Employee();
        employee.setId(20);

        Offer offer = new Offer();
        offer.setOfferPrice(200L);
        offer.setEmployee(employee);

        Credit employeeCredit = new Credit();
        employeeCredit.setId(200);
        employeeCredit.setAmount(100.0);


        when(orderService.findById(ordersId)).thenReturn(order);
        when(customerRepository.findById(customerId, Customer.class)).thenReturn(customer);
        when(offerService.findAcceptedOfferInOrder(ordersId)).thenReturn(offer);
        when(creditService.findCreditById(customerCredit.getId())).thenReturn(customerCredit);
        when(creditService.findByEmployeeId(employee.getId())).thenReturn(employeeCredit);


        String result = customerService.customerPay(ordersId, customerId);


        assertEquals("successful", result);
        // customer: 500 - 200 = 300
        assertEquals(300.0, customerCredit.getAmount());
        // employee: 100 + 0.7 * 200 = 240
        assertEquals(240.0, employeeCredit.getAmount());
        // order must be PAID
        assertSame(order.getOrderState(), OrderState.PAID);


        verify(orderService).findById(ordersId);
        verify(customerRepository).findById(customerId, Customer.class);
        verify(offerService).findAcceptedOfferInOrder(ordersId);
        verify(creditService).findCreditById(customerCredit.getId());
        verify(creditService).findByEmployeeId(employee.getId());
        verify(creditService, times(1)).update(customerCredit);
        verify(creditService, times(1)).update(employeeCredit);
        verify(orderService, times(1)).update(order);
    }

    @Test
    void getCreditAmount_shouldReturnAmount_whenCreditHasAmount()
    {
        int customerId = 1;

        Credit credit = new Credit();
        credit.setAmount(250.0);

        when(creditService.findByCustomerId(customerId)).thenReturn(credit);

        Double result = customerService.getCreditAmount(customerId);

        assertEquals(250.0, result);
        verify(creditService, times(1)).findByCustomerId(customerId);
    }

    @Test
    void getCreditAmount_shouldThrowNotFoundCustomer_whenAmountIsNull()
    {
        int customerId = 1;

        Credit credit = new Credit();
        credit.setAmount(null);   // simulate bad data

        when(creditService.findByCustomerId(customerId)).thenReturn(credit);

        assertThrows(
                NotFoundCustomer.class,
                () -> customerService.getCreditAmount(customerId)
        );
    }

    @Test
    void validateCustomerEmail_shouldActivateCustomerAndExpireToken_whenTokenAndCustomerAreValid()
    {
        String token = "abc123";
        String email = "test@example.com";

        EmailToken emailToken = new EmailToken();
        emailToken.setToken(token);
        emailToken.setEmail(email);
        emailToken.setExpired(false);

        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setActive(false);

        when(emailTokenService.findByToken(token)).thenReturn(emailToken);
        when(customerRepository.findByEmail(email)).thenReturn(customer);

        String result = customerService.validateCustomerEmail(token);

        assertEquals("successful", result);
        assertTrue(customer.isActive());
        assertTrue(emailToken.isExpired());

        verify(emailTokenService, times(1)).validateToken(token);
        verify(customerRepository, times(1)).findByEmail(email);
    }

    @Test
    void validateCustomerEmail_shouldThrowInvalidTokenExceptions_whenTokenNotFound()
    {
        String token = "invalid-token";

        when(emailTokenService.findByToken(token)).thenReturn(null);

        assertThrows(
                InvalidTokenExceptions.class,
                () -> customerService.validateCustomerEmail(token)
        );

        verify(emailTokenService, never()).validateToken(anyString());
        verify(customerRepository, never()).findByEmail(anyString());
    }

    @Test
    void validateCustomerEmail_shouldThrowInvalidTokenExceptions_whenCustomerNotFoundForTokenEmail()
    {
        String token = "abc123";
        String email = "test@example.com";

        EmailToken emailToken = new EmailToken();
        emailToken.setToken(token);
        emailToken.setEmail(email);
        emailToken.setExpired(false);

        when(emailTokenService.findByToken(token)).thenReturn(emailToken);
        when(customerRepository.findByEmail(email)).thenReturn(null);

        assertThrows(
                InvalidTokenExceptions.class,
                () -> customerService.validateCustomerEmail(token)
        );

        verify(emailTokenService, times(1)).validateToken(token);
        verify(customerRepository, times(1)).findByEmail(email);
    }

    @Test
    void customerChargeCart_shouldCreateCartAndCharge_whenCartDoesNotExistAndEnoughMoney()
    {
        Integer customerId = 1;

        PayToCartDto dto = new PayToCartDto(
                customerId,
                200.0,
                "1111-2222-3333-4444",
                "123",
                LocalDate.of(2022,11, 2),
                null,
                null
        );

        Credit credit = new Credit();
        credit.setAmount(100.0);

        when(creditService.findByCustomerId(customerId)).thenReturn(credit);
        when(customerCartService.findCustomerCartByCustomerId(customerId)).thenReturn(null);

        String result = customerService.customerChargeCart(dto);

        assertEquals("successful", result);
        assertEquals(300.0, credit.getAmount());

        ArgumentCaptor<CustomerCart> cartCaptor = ArgumentCaptor.forClass(CustomerCart.class);
        verify(customerCartService).updateCustomerCart(cartCaptor.capture());
        CustomerCart savedCart = cartCaptor.getValue();

        assertEquals(1000.0, savedCart.getMoney());
        assertNotNull(savedCart.getCustomer());
        assertEquals(customerId, savedCart.getCustomer().getId());

        verify(creditService, times(1)).update(credit);
    }

    @Test
    void customerChargeCart_shouldUseExistingCartAndCharge_whenCartExistsAndEnoughMoney()
    {
        Integer customerId = 1;

        PayToCartDto dto = new PayToCartDto(
                customerId,
                200.0,
                "1111-2222-3333-4444",
                "123",
                LocalDate.of(2022,11, 2),
                null,
                null
        );

        Credit credit = new Credit();
        credit.setAmount(100.0);

        CustomerCart existingCart = new CustomerCart();
        existingCart.setMoney(300.0); // existing balance

        when(creditService.findByCustomerId(customerId)).thenReturn(credit);
        when(customerCartService.findCustomerCartByCustomerId(customerId)).thenReturn(existingCart);

        String result = customerService.customerChargeCart(dto);

        assertEquals("successful", result);
        // credit: 100 + 200 = 300
        assertEquals(300.0, credit.getAmount());
        // cart: 300 - 200 = 100
        assertEquals(100.0, existingCart.getMoney());

        verify(creditService, times(1)).update(credit);
        verify(customerCartService, times(1)).updateCustomerCart(existingCart);
    }

    @Test
    void customerChargeCart_shouldThrowDontHaveEnoughMoney_whenCartDoesNotHaveEnoughBalance()
    {
        Integer customerId = 1;

        PayToCartDto dto = new PayToCartDto(
                customerId,
                200.0,
                "1111-2222-3333-4444",
                "123",
                LocalDate.of(2022,11, 2),
                null,
                null
        );

        Credit credit = new Credit();
        credit.setAmount(100.0);

        CustomerCart existingCart = new CustomerCart();
        existingCart.setMoney(150.0); // 150 - 200 < 0 → exception

        when(creditService.findByCustomerId(customerId)).thenReturn(credit);
        when(customerCartService.findCustomerCartByCustomerId(customerId)).thenReturn(existingCart);

        assertThrows(
                DontHaveEnoughMoney.class,
                () -> customerService.customerChargeCart(dto)
        );

        verify(creditService, never()).update(any());
        verify(customerCartService, never()).updateCustomerCart(any());
    }

    @Test
    void getOffersForOrder_shouldReturnOfferDtos_whenOrderBelongsToCustomerAndOffersExist()
    {
        Integer customerId = 1;
        Integer orderId = 10;
        Authentication auth =
                new UsernamePasswordAuthenticationToken(customerId, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Customer customer = new Customer();
        customer.setId(customerId);

        Orders order = new Orders();
        order.setId(orderId);
        order.setCustomer(customer);

        Employee employee = new Employee();
        employee.setId(5);
        employee.setName("John");
        employee.setLast_name("Doe");
        employee.setScore(4);

        Offer offer1 = new Offer();
        offer1.setId(100);
        offer1.setOfferPrice(200L);
        offer1.setEmployee(employee);
        offer1.setTimeOfWork(java.time.LocalDateTime.now());
        offer1.setWorkTimeInMinutes(60);
        offer1.setAccepted(true);

        java.util.List<Offer> offers = java.util.List.of(offer1);

        when(orderService.findById(orderId)).thenReturn(order);
        when(offerService.findAllOffersForSpecificOrder(orderId)).thenReturn(offers);

        java.util.List<OfferDtoForCustomer> result =
                customerService.getOffersForOrder(orderId);

        assertEquals(1, result.size());
        OfferDtoForCustomer dto = result.get(0);
        assertEquals(offer1.getId(), dto.id());
        assertEquals(offer1.getOfferPrice(), dto.offerPrice());
        assertEquals(employee.getScore(), dto.employeeScore());
        assertEquals("John Doe", dto.employeeName());
        assertEquals(offer1.getTimeOfWork(), dto.timeOfWork());
        assertEquals(offer1.getWorkTimeInMinutes(), dto.workInMinutes());
        assertTrue(dto.accepted());

        verify(orderService, times(1)).findById(orderId);
        verify(offerService, times(1)).findAllOffersForSpecificOrder(orderId);
    }

    @Test
    void getOffersForOrder_shouldThrowNotFoundOrder_whenOrderDoesNotBelongToAuthenticatedCustomer()
    {
        Integer realCustomerId = 1;
        Integer otherCustomerId = 2;
        Integer orderId = 10;

        Authentication auth =
                new UsernamePasswordAuthenticationToken(realCustomerId, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Customer otherCustomer = new Customer();
        otherCustomer.setId(otherCustomerId);

        Orders order = new Orders();
        order.setId(orderId);
        order.setCustomer(otherCustomer);

        when(orderService.findById(orderId)).thenReturn(order);

        assertThrows(
                org.example.exeptions.NotFoundException.NotFoundOrder.class,
                () -> customerService.getOffersForOrder(orderId)
        );

        verify(offerService, never()).findAllOffersForSpecificOrder(anyInt());
    }

    @Test
    void getOffersForOrder_shouldThrowNotFoundOffer_whenNoOffersFound()
    {
        Integer customerId = 1;
        Integer orderId = 10;

        Authentication auth =
                new UsernamePasswordAuthenticationToken(customerId, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Customer customer = new Customer();
        customer.setId(customerId);

        Orders order = new Orders();
        order.setId(orderId);
        order.setCustomer(customer);

        when(orderService.findById(orderId)).thenReturn(order);
        when(offerService.findAllOffersForSpecificOrder(orderId)).thenReturn(null);

        assertThrows(
                org.example.exeptions.NotFoundException.NotFoundOffer.class,
                () -> customerService.getOffersForOrder(orderId)
        );
    }

    @Test
    void startOrder_shouldSetOrderStateToStarted_whenAllConditionsAreValid()
    {
        Integer customerId = 1;
        Integer orderId = 10;

        Authentication auth =
                new UsernamePasswordAuthenticationToken(customerId, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Customer customer = new Customer();
        customer.setId(customerId);

        Orders order = new Orders();
        order.setId(orderId);
        order.setCustomer(customer);
        order.setOrderState(OrderState.UNDER_REACHING_EMPLOYEE);

        Offer offer = new Offer();
        offer.setTimeOfWork(LocalDateTime.now().minusMinutes(5));

        when(orderService.findById(orderId)).thenReturn(order);
        when(offerService.findAcceptedOfferInOrder(orderId)).thenReturn(offer);

        customerService.startOrder(orderId);

        assertEquals(OrderState.STARTED, order.getOrderState());
        verify(orderService, times(1)).update(order);
    }

    @Test
    void startOrder_shouldThrowNotFoundOrder_whenOrderDoesNotExist()
    {
        int orderId = 10;

        when(orderService.findById(orderId)).thenReturn(null);

        assertThrows(
                NotFoundOrder.class,
                () -> customerService.startOrder(orderId)
        );

        verify(orderService, never()).update(any());
    }

    @Test
    void startOrder_shouldThrowNotFoundOrder_whenOrderDoesNotBelongToAuthenticatedCustomer()
    {
        Integer realCustomerId = 1;
        Integer otherCustomerId = 2;
        Integer orderId = 10;

        Authentication auth =
                new UsernamePasswordAuthenticationToken(realCustomerId, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Customer otherCustomer = new Customer();
        otherCustomer.setId(otherCustomerId);

        Orders order = new Orders();
        order.setId(orderId);
        order.setCustomer(otherCustomer);
        order.setOrderState(OrderState.UNDER_REACHING_EMPLOYEE);

        when(orderService.findById(orderId)).thenReturn(order);

        assertThrows(
                NotFoundOrder.class,
                () -> customerService.startOrder(orderId)
        );

        verify(orderService, never()).update(any());
        verify(offerService, never()).findAcceptedOfferInOrder(anyInt());
    }

    @Test
    void startOrder_shouldThrowOrderStateIsNotCorrect_whenOrderStateIsNotUnderReachingEmployee()
    {
        Integer customerId = 1;
        Integer orderId = 10;

        Authentication auth =
                new UsernamePasswordAuthenticationToken(customerId, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Customer customer = new Customer();
        customer.setId(customerId);

        Orders order = new Orders();
        order.setId(orderId);
        order.setCustomer(customer);
        order.setOrderState(OrderState.DONE); // wrong state

        when(orderService.findById(orderId)).thenReturn(order);

        assertThrows(
                OrderStateIsNotCorrect.class,
                () -> customerService.startOrder(orderId)
        );

        verify(orderService, never()).update(any());
        verify(offerService, never()).findAcceptedOfferInOrder(anyInt());
    }

    @Test
    void startOrder_shouldThrowNotFoundOffer_whenNoAcceptedOfferFound()
    {
        Integer customerId = 1;
        Integer orderId = 10;

        Authentication auth =
                new UsernamePasswordAuthenticationToken(customerId, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Customer customer = new Customer();
        customer.setId(customerId);

        Orders order = new Orders();
        order.setId(orderId);
        order.setCustomer(customer);
        order.setOrderState(OrderState.UNDER_REACHING_EMPLOYEE);

        when(orderService.findById(orderId)).thenReturn(order);
        when(offerService.findAcceptedOfferInOrder(orderId)).thenReturn(null);

        assertThrows(
                NotFoundOffer.class,
                () -> customerService.startOrder(orderId)
        );

        verify(orderService, never()).update(any());
    }

    @Test
    void startOrder_shouldThrowTimeOfWorkDoesntMatch_whenOfferTimeIsInFuture()
    {
        Integer customerId = 1;
        Integer orderId = 10;

        Authentication auth =
                new UsernamePasswordAuthenticationToken(customerId, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Customer customer = new Customer();
        customer.setId(customerId);

        Orders order = new Orders();
        order.setId(orderId);
        order.setCustomer(customer);
        order.setOrderState(OrderState.UNDER_REACHING_EMPLOYEE);

        Offer offer = new Offer();
        offer.setTimeOfWork(LocalDateTime.now().plusMinutes(10)); // future

        when(orderService.findById(orderId)).thenReturn(order);
        when(offerService.findAcceptedOfferInOrder(orderId)).thenReturn(offer);

        assertThrows(
                TimeOfWorkDoesntMatch.class,
                () -> customerService.startOrder(orderId)
        );

        verify(orderService, never()).update(any());
    }

    @Test
    void addComment_shouldSetCommentAndScoreAndIncreaseEmployeeScore_whenDataIsValid()
    {
        Integer customerId = 1;
        Integer orderId = 10;
        Integer star = 4;
        String comment = "Great job";

        Authentication auth =
                new UsernamePasswordAuthenticationToken(customerId, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Customer customer = new Customer();
        customer.setId(customerId);

        Employee employee = new Employee();
        employee.setId(5);
        employee.setName("John");
        employee.setLast_name("Doe");
        employee.setScore(10);

        Orders order = new Orders();
        order.setId(orderId);
        order.setCustomer(customer);
        order.setEmployee(employee);
        order.setOrderState(OrderState.PAID);
        order.setScore(null);

        when(orderService.findById(orderId)).thenReturn(order);

        customerService.addComment(orderId, star, comment);

        assertEquals(comment, order.getComment());
        assertEquals(star, order.getScore());
        assertEquals(10 + star, employee.getScore());
        verify(orderService, times(1)).update(order);
    }

    @Test
    void addComment_shouldThrowNotFoundOrder_whenOrderNotFound()
    {
        int orderId = 10;
        Integer star = 4;

        when(orderService.findById(orderId)).thenReturn(null);

        assertThrows(
                NotFoundOrder.class,
                () -> customerService.addComment(orderId, star, "comment")
        );

        verify(orderService, never()).update(any());
    }

    @Test
    void addComment_shouldThrowNotFoundOrder_whenOrderDoesNotBelongToAuthenticatedCustomer()
    {
        Integer realCustomerId = 1;
        Integer otherCustomerId = 2;
        Integer orderId = 10;

        Authentication auth =
                new UsernamePasswordAuthenticationToken(realCustomerId, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Customer otherCustomer = new Customer();
        otherCustomer.setId(otherCustomerId);

        Employee employee = new Employee();
        employee.setScore(5);

        Orders order = new Orders();
        order.setId(orderId);
        order.setCustomer(otherCustomer);
        order.setEmployee(employee);
        order.setOrderState(OrderState.PAID);
        order.setScore(null);

        when(orderService.findById(orderId)).thenReturn(order);

        assertThrows(
                NotFoundOrder.class,
                () -> customerService.addComment(orderId, 3, "comment")
        );

        verify(orderService, never()).update(any());
    }

    @Test
    void addComment_shouldThrowDuplicateCommentException_whenScoreAlreadySet()
    {
        Integer customerId = 1;
        Integer orderId = 10;

        Authentication auth =
                new UsernamePasswordAuthenticationToken(customerId, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Customer customer = new Customer();
        customer.setId(customerId);

        Employee employee = new Employee();
        employee.setScore(5);

        Orders order = new Orders();
        order.setId(orderId);
        order.setCustomer(customer);
        order.setEmployee(employee);
        order.setOrderState(OrderState.PAID);
        order.setScore(3); // already has score

        when(orderService.findById(orderId)).thenReturn(order);

        assertThrows(
                DuplicateCommentException.class,
                () -> customerService.addComment(orderId, 4, "new comment")
        );

        verify(orderService, never()).update(any());
    }

    @Test
    void addComment_shouldThrowOrderStateIsNotCorrect_whenOrderIsNotPaid()
    {
        Integer customerId = 1;
        Integer orderId = 10;

        Authentication auth =
                new UsernamePasswordAuthenticationToken(customerId, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Customer customer = new Customer();
        customer.setId(customerId);

        Employee employee = new Employee();
        employee.setScore(5);

        Orders order = new Orders();
        order.setId(orderId);
        order.setCustomer(customer);
        order.setEmployee(employee);
        order.setOrderState(OrderState.STARTED);
        order.setScore(null);

        when(orderService.findById(orderId)).thenReturn(order);

        assertThrows(
                OrderStateIsNotCorrect.class,
                () -> customerService.addComment(orderId, 4, "comment")
        );

        verify(orderService, never()).update(any());
    }

    @Test
    void addComment_shouldThrowStarShouldBeenBetween1To5_whenStarIsOutOfRange()
    {
        Integer customerId = 1;
        Integer orderId = 10;
        Integer invalidStar = 6;

        Authentication auth =
                new UsernamePasswordAuthenticationToken(customerId, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Customer customer = new Customer();
        customer.setId(customerId);

        Employee employee = new Employee();
        employee.setScore(5);

        Orders order = new Orders();
        order.setId(orderId);
        order.setCustomer(customer);
        order.setEmployee(employee);
        order.setOrderState(OrderState.PAID);
        order.setScore(null);

        when(orderService.findById(orderId)).thenReturn(order);

        assertThrows(
                StarShouldBeenBetween1To5.class,
                () -> customerService.addComment(orderId, invalidStar, "comment")
        );

        verify(orderService, never()).update(any());
    }

    @Test
    void customerAcceptOffer_shouldDelegateToCombinedUserClass()
    {
        Integer offerId = 10;

        customerService.customerAcceptOffer(offerId);

        verify(combineUserClass, times(1)).acceptOffer(offerId);
        verifyNoMoreInteractions(combineUserClass);
    }

    @Test
    void makeServiceStateToDone_shouldCallMakeTheOrderDoneAndReturnSuccessful()
    {
        Integer orderId = 10;

        String result = customerService.makeServiceStateToDone(orderId);

        assertEquals("successful", result);
        verify(combineUserClass, times(1)).makeTheOrderDone(orderId);
        verifyNoMoreInteractions(combineUserClass);
    }

    @Test
    void findByEmail_shouldReturnCustomerFromRepository()
    {
        String email = "test@example.com";

        Customer customer = new Customer();
        customer.setEmail(email);

        when(customerRepository.findByEmail(email)).thenReturn(customer);

        Customer result = customerService.findByEmail(email);

        assertSame(customer, result);
        verify(customerRepository, times(1)).findByEmail(email);
    }

    @Test
    void optionalSelectOrdersForCustomer_shouldMapOrdersToDtoCorrectly()
    {
        Integer customerId = 1;
        String state = "PAID";

        Orders order = new Orders();
        order.setId(10);
        order.setOfferedPrice(200.0);
        order.setDetail("cleaning");
        LocalDateTime timeOfWork = LocalDateTime.now().plusDays(1);
        order.setTimeOfWork(timeOfWork);
        order.setAddress("Tehran");
        order.setOrderState(OrderState.PAID);
        order.setScore(5);
        order.setComment("perfect");

        when(orderService.optionalFindOrdersForCustomer(customerId, state))
                .thenReturn(java.util.List.of(order));

        java.util.List<OrdersOutputDtoUser> result =
                customerService.optionalSelectOrdersForCustomer(customerId, state);

        assertEquals(1, result.size());
        OrdersOutputDtoUser dto = result.get(0);

        assertEquals(order.getId(), dto.orderId());
        assertEquals(order.getOfferedPrice(), dto.offerPrice());
        assertEquals(order.getDetail(), dto.detail());
        assertEquals(order.getTimeOfWork(), dto.timeOfWork());
        assertEquals(order.getAddress(), dto.address());
        assertEquals(order.getOrderState(), dto.orderState());
        assertEquals(order.getScore(), dto.score());
        assertEquals(order.getComment(), dto.comment());

        verify(orderService, times(1)).optionalFindOrdersForCustomer(customerId, state);
    }

    @Test
    void optionalSelectOrdersForCustomer_shouldReturnEmptyList_whenNoOrdersFound()
    {
        Integer customerId = 1;
        String state = "WAITING";

        when(orderService.optionalFindOrdersForCustomer(customerId, state))
                .thenReturn(java.util.Collections.emptyList());

        java.util.List<OrdersOutputDtoUser> result =
                customerService.optionalSelectOrdersForCustomer(customerId, state);

        assertTrue(result.isEmpty());
        verify(orderService, times(1)).optionalFindOrdersForCustomer(customerId, state);
    }

    private Offer buildOffer(long price, int score, String employeeName)
    {
        Employee employee = new Employee();
        employee.setScore(score);
        String[] parts = employeeName.split(" ");
        employee.setName(parts[0]);
        employee.setLast_name(parts.length > 1 ? parts[1] : "");

        Offer offer = new Offer();
        offer.setOfferPrice(price);
        offer.setEmployee(employee);
        offer.setTimeOfWork(LocalDateTime.now());
        offer.setWorkTimeInMinutes(60);
        return offer;
    }

    @Test
    void sortedOfferForCustomer_shouldSortByScoreAscending_whenSortByScoreTrueAndAscendingFalse()
    {
        Integer customerId = 1;
        Integer orderId = 10;

        Authentication auth =
                new UsernamePasswordAuthenticationToken(customerId, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Customer customer = new Customer();
        customer.setId(customerId);

        Orders order = new Orders();
        order.setId(orderId);
        order.setCustomer(customer);

        Offer offer1 = buildOffer(300L, 5, "A High");
        Offer offer2 = buildOffer(200L, 1, "B Low");
        Offer offer3 = buildOffer(400L, 3, "C Mid");

        when(orderService.findById(orderId)).thenReturn(order);
        when(offerService.findAllOffersForSpecificOrder(orderId))
                .thenReturn(List.of(offer1, offer2, offer3));

        SortingOffer input = new SortingOffer(customerId, orderId, true, false);

        List<SortedOfferDtoForCustomer> result =
                customerService.sortedOfferForCustomer(input);

        assertEquals(3, result.size());
        assertEquals(1, result.get(0).employeeScore());
        assertEquals(3, result.get(1).employeeScore());
        assertEquals(5, result.get(2).employeeScore());

        assertTrue(result.get(0).ascending());
        assertTrue(result.get(1).ascending());
        assertTrue(result.get(2).ascending());
        assertTrue(result.get(0).sortByScore());
    }

    @Test
    void sortedOfferForCustomer_shouldSortByScoreDescending_whenSortByScoreTrueAndAscendingTrue()
    {
        Integer customerId = 1;
        Integer orderId = 10;

        Authentication auth =
                new UsernamePasswordAuthenticationToken(customerId, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Customer customer = new Customer();
        customer.setId(customerId);

        Orders order = new Orders();
        order.setId(orderId);
        order.setCustomer(customer);

        Offer offer1 = buildOffer(300L, 5, "A High");
        Offer offer2 = buildOffer(200L, 1, "B Low");
        Offer offer3 = buildOffer(400L, 3, "C Mid");

        when(orderService.findById(orderId)).thenReturn(order);
        when(offerService.findAllOffersForSpecificOrder(orderId))
                .thenReturn(List.of(offer1, offer2, offer3));

        SortingOffer input = new SortingOffer(customerId, orderId, true, true);

        List<SortedOfferDtoForCustomer> result =
                customerService.sortedOfferForCustomer(input);

        assertEquals(3, result.size());
        assertEquals(5, result.get(0).employeeScore());
        assertEquals(3, result.get(1).employeeScore());
        assertEquals(1, result.get(2).employeeScore());

        assertFalse(result.get(0).ascending());
        assertTrue(result.get(0).sortByScore());
    }

    @Test
    void sortedOfferForCustomer_shouldSortByPriceAscending_whenSortByScoreFalseAndAscendingFalse()
    {
        Integer customerId = 1;
        Integer orderId = 10;

        Authentication auth =
                new UsernamePasswordAuthenticationToken(customerId, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Customer customer = new Customer();
        customer.setId(customerId);

        Orders order = new Orders();
        order.setId(orderId);
        order.setCustomer(customer);

        Offer offer1 = buildOffer(300L, 5, "A");
        Offer offer2 = buildOffer(100L, 3, "B");
        Offer offer3 = buildOffer(200L, 1, "C");

        when(orderService.findById(orderId)).thenReturn(order);
        when(offerService.findAllOffersForSpecificOrder(orderId))
                .thenReturn(List.of(offer1, offer2, offer3));

        SortingOffer input = new SortingOffer(customerId, orderId, false, false);

        List<SortedOfferDtoForCustomer> result =
                customerService.sortedOfferForCustomer(input);

        assertEquals(3, result.size());
        assertEquals(100L, result.get(0).offerPrice());
        assertEquals(200L, result.get(1).offerPrice());
        assertEquals(300L, result.get(2).offerPrice());

        assertTrue(result.get(0).ascending());
        assertFalse(result.get(0).sortByScore());
    }

    @Test
    void sortedOfferForCustomer_shouldSortByPriceDescending_whenSortByScoreFalseAndAscendingTrue()
    {
        Integer customerId = 1;
        Integer orderId = 10;

        Authentication auth =
                new UsernamePasswordAuthenticationToken(customerId, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Customer customer = new Customer();
        customer.setId(customerId);

        Orders order = new Orders();
        order.setId(orderId);
        order.setCustomer(customer);

        Offer offer1 = buildOffer(300L, 5, "A");
        Offer offer2 = buildOffer(100L, 3, "B");
        Offer offer3 = buildOffer(200L, 1, "C");

        when(orderService.findById(orderId)).thenReturn(order);
        when(offerService.findAllOffersForSpecificOrder(orderId))
                .thenReturn(List.of(offer1, offer2, offer3));

        SortingOffer input = new SortingOffer(customerId, orderId, false, true);

        List<SortedOfferDtoForCustomer> result =
                customerService.sortedOfferForCustomer(input);

        assertEquals(3, result.size());
        assertEquals(300L, result.get(0).offerPrice());
        assertEquals(200L, result.get(1).offerPrice());
        assertEquals(100L, result.get(2).offerPrice());

        assertFalse(result.get(0).ascending());
        assertFalse(result.get(0).sortByScore());
    }

    @Test
    void sortedOfferForCustomer_shouldThrowNotFoundOrder_whenOrderNotFound()
    {
        Integer customerId = 1;
        int orderId = 10;
        SortingOffer input = new SortingOffer(customerId, orderId, true, true);

        when(orderService.findById(orderId)).thenReturn(null);

        assertThrows(
                NotFoundOrder.class,
                () -> customerService.sortedOfferForCustomer(input)
        );

        verify(offerService, never()).findAllOffersForSpecificOrder(anyInt());
    }

    @Test
    void getHandlersForCustomer_shouldReturnMappedHandlerCustomerDtos()
    {
        Handler h1 = new Handler();
        h1.setId(1);
        h1.setName("Cleaning");

        Handler h2 = new Handler();
        h2.setId(2);
        h2.setName("Plumbing");

        when(handlerService.findAllHandlers()).thenReturn(java.util.List.of(h1, h2));

        java.util.List<HandlerCustomerDto> result = customerService.getHandlersForCustomer();

        assertEquals(2, result.size());

        HandlerCustomerDto dto1 = result.get(0);
        assertEquals(1, dto1.id());
        assertEquals("Cleaning", dto1.name());

        HandlerCustomerDto dto2 = result.get(1);
        assertEquals(2, dto2.id());
        assertEquals("Plumbing", dto2.name());

        verify(handlerService, times(1)).findAllHandlers();
    }

    @Test
    void getSubHandlersForHandler_shouldReturnMappedDtos_whenSubHandlersExist()
    {
        Integer handlerId = 10;

        SubHandler s1 = new SubHandler();
        s1.setId(1);
        s1.setName("Kitchen cleaning");
        s1.setDetail("Full kitchen");
        s1.setBasePrice(100.0);

        SubHandler s2 = new SubHandler();
        s2.setId(2);
        s2.setName("Bathroom cleaning");
        s2.setDetail("Full bathroom");
        s2.setBasePrice(150.0);

        when(subHandlerService.findAllSubHandlerSameHandler(handlerId))
                .thenReturn(java.util.List.of(s1, s2));

        java.util.List<SubHandlersDtoOutputId> result =
                customerService.getSubHandlersForHandler(handlerId);

        assertEquals(2, result.size());

        SubHandlersDtoOutputId dto1 = result.get(0);
        assertEquals(1, dto1.id());
        assertEquals("Kitchen cleaning", dto1.name());
        assertEquals("Full kitchen", dto1.detail());
        assertEquals(100.0, dto1.offerPrice());

        SubHandlersDtoOutputId dto2 = result.get(1);
        assertEquals(2, dto2.id());
        assertEquals("Bathroom cleaning", dto2.name());
        assertEquals("Full bathroom", dto2.detail());
        assertEquals(150.0, dto2.offerPrice());

        verify(subHandlerService, times(1)).findAllSubHandlerSameHandler(handlerId);
    }

    @Test
    void getSubHandlersForHandler_shouldReturnNull_whenRepositoryReturnsNull()
    {
        Integer handlerId = 10;

        when(subHandlerService.findAllSubHandlerSameHandler(handlerId))
                .thenReturn(null);

        java.util.List<SubHandlersDtoOutputId> result =
                customerService.getSubHandlersForHandler(handlerId);

        assertNull(result);
        verify(subHandlerService, times(1)).findAllSubHandlerSameHandler(handlerId);
    }

    @Test
    void getAllOrders_shouldMapOrdersToDtoIncludingEmployeeName()
    {
        Integer customerId = 1;
        Orders order1 = new Orders();
        order1.setId(10);
        order1.setOfferedPrice(200d);
        order1.setDetail("Cleaning");
        order1.setTimeOfWork(LocalDateTime.now().plusDays(1));
        order1.setAddress("Tehran");
        order1.setOrderState(OrderState.PAID);

        Customer customer = new Customer();
        customer.setId(customerId);
        order1.setCustomer(customer);

        Employee emp = new Employee();
        emp.setId(5);
        emp.setName("John");
        emp.setLast_name("Doe");
        order1.setEmployee(emp);

        SubHandler sub = new SubHandler();
        sub.setName("Kitchen");
        order1.setSubHandler(sub);
        Orders order2 = new Orders();
        order2.setId(11);
        order2.setOfferedPrice(300d);
        order2.setDetail("Plumbing");
        order2.setTimeOfWork(LocalDateTime.now().plusDays(2));
        order2.setAddress("Mashhad");
        order2.setOrderState(OrderState.WAITING_FOR_EMPLOYEE_OFFER);
        order2.setCustomer(customer);

        SubHandler sub2 = new SubHandler();
        sub2.setName(null); // test null-name condition
        order2.setSubHandler(sub2);

        when(orderService.findAllOrdersThatHaveSameCustomer(customerId))
                .thenReturn(java.util.List.of(order1, order2));

        java.util.List<OrdersOutputDtoCustomer> result =
                customerService.getAllOrders(customerId);

        assertEquals(2, result.size());

        OrdersOutputDtoCustomer dto1 = result.get(0);
        assertEquals(10, dto1.id());
        assertEquals(5, dto1.employeeId());
        assertEquals("John Doe", dto1.employeeName());
        assertEquals(order1.getOfferedPrice(), dto1.offerPrice());
        assertEquals("Kitchen", dto1.subHandlerName());

        OrdersOutputDtoCustomer dto2 = result.get(1);
        assertEquals(11, dto2.id());
        assertNull(dto2.employeeId());
        assertNull(dto2.employeeName());
        assertEquals("", dto2.subHandlerName());

        verify(orderService, times(1)).findAllOrdersThatHaveSameCustomer(customerId);
    }

    @Test
    void findCustomerByOptional_shouldDelegateToRepositoryAndReturnResult()
    {
        FindFilteredCustomerDto input =
                new FindFilteredCustomerDto("Ali", "Ahmadi", "a@test.com", "09120000000");

        Customer c1 = new Customer();
        Customer c2 = new Customer();

        java.util.List<Customer> customers = java.util.List.of(c1, c2);

        when(customerRepository.selectCustomerByOptional(
                input.name(), input.lastName(), input.email(), input.phone()
        )).thenReturn(customers);

        java.util.List<Customer> result =
                customerService.findCustomerByOptional(input);

        assertSame(customers, result);
        verify(customerRepository, times(1))
                .selectCustomerByOptional(input.name(), input.lastName(), input.email(), input.phone());
    }

    @Test
    void findCustomerByReports_shouldDelegateToRepositoryAndReturnResult()
    {
        FindCustomerByFilterDto input =
                new FindCustomerByFilterDto(
                        LocalDate.now().minusDays(10),
                        LocalDate.now(),
                        1,
                        10
                );

        CustomerOutputDtoForReport dto1 =
                new CustomerOutputDtoForReport(
                        1,
                        "Ali",
                        "Ahmadi",
                        "a@test.com",
                        "09120000000",
                        LocalDateTime.now().minusDays(5),
                        true,
                        5L
                );

        CustomerOutputDtoForReport dto2 =
                new CustomerOutputDtoForReport(
                        2,
                        "Sara",
                        "Mohammadi",
                        "s@test.com",
                        "09123334444",
                        LocalDateTime.now().minusDays(3),
                        false,
                        3L
                );

        java.util.List<CustomerOutputDtoForReport> list =
                java.util.List.of(dto1, dto2);
        when(customerRepository.selectCustomerByReports(
                input.startDate(),
                input.endDate(),
                input.doneOrderStart(),
                input.doneOrderEnd()
        )).thenReturn(list);

        java.util.List<CustomerOutputDtoForReport> result =
                customerService.findCustomerByReports(input);
        assertSame(list, result);
        verify(customerRepository, times(1))
                .selectCustomerByReports(
                        input.startDate(),
                        input.endDate(),
                        input.doneOrderStart(),
                        input.doneOrderEnd()
                );
    }

    @Test
    void createOrder_shouldSaveOrderAndReturnDto_whenDataIsValid()
    {
        Integer customerId = 1;
        Integer subHandlerId = 2;
        LocalDateTime timeOfWork = LocalDateTime.now().plusHours(1);
        OrderDto orderDto = new OrderDto(
                300.0,
                "detail",
                timeOfWork,
                "Tehran",
                subHandlerId,
                customerId
        );

        Customer customer = new Customer();
        customer.setId(customerId);

        SubHandler subHandler = new SubHandler();
        subHandler.setId(subHandlerId);
        subHandler.setBasePrice(200.0);

        Orders mappedOrder = new Orders();

        when(customerRepository.findById(customerId, Customer.class)).thenReturn(customer);
        when(subHandlerService.findSubHandlerById(subHandlerId)).thenReturn(subHandler);
        when(entityMapper.dtoToOrder(orderDto)).thenReturn(mappedOrder);

        OrderDto result = customerService.createOrder(orderDto);

        assertSame(orderDto, result);
        assertEquals(OrderState.WAITING_FOR_EMPLOYEE_OFFER, mappedOrder.getOrderState());
        assertSame(customer, mappedOrder.getCustomer());
        assertSame(subHandler, mappedOrder.getSubHandler());
        verify(orderService, times(1)).save(mappedOrder);
    }

    private static Stream<Arguments> invalidCreateOrderCases()
    {
        LocalDateTime past = LocalDateTime.now().minusHours(1);
        LocalDateTime future = LocalDateTime.now().plusHours(1);

        return Stream.of(
                // 1) time before now -> TimeOfWorkDoesntMatch
                Arguments.of(
                        "time before now",
                        300.0,
                        past,
                        true,
                        true,
                        200.0,
                        TimeOfWorkDoesntMatch.class
                ),

                Arguments.of(
                        "price lower than base",
                        100.0,
                        future,
                        true,
                        true,
                        200.0,
                        OrderPriceShouldBeHigherThanBase.class
                ),

                Arguments.of(
                        "customer not found",
                        300.0,
                        future,
                        false,
                        false,
                        200.0,
                        NotFoundCustomer.class
                ),
                // 4) subHandler not found -> NotFoundHandler
                Arguments.of(
                        "subHandler not found",
                        300.0,
                        future,
                        true,
                        false,
                        200.0,
                        NotFoundHandler.class
                )
        );
    }

    private static final Integer CUSTOMER_ID = 1;
    private static final Integer SUB_HANDLER_ID = 2;

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidCreateOrderCases")
    void createOrder_invalidCases_shouldThrow(
            String caseName,
            Double offeredPrice,
            LocalDateTime timeOfWork,
            boolean customerExists,
            boolean subHandlerExists,
            double subHandlerBasePrice,
            Class<? extends Throwable> expectedException
    )
    {
        OrderDto orderDto = buildOrderDto(offeredPrice, timeOfWork);

        stubCustomer(customerExists);
        stubSubHandler(subHandlerExists, subHandlerBasePrice, customerExists);

        assertThrows(
                expectedException,
                () -> customerService.createOrder(orderDto)
        );

        verify(orderService, never()).save(any());
    }

    private OrderDto buildOrderDto(Double offeredPrice, LocalDateTime timeOfWork)
    {
        return new OrderDto(
                offeredPrice,
                "detail",
                timeOfWork,
                "Tehran",
                SUB_HANDLER_ID,
                CUSTOMER_ID
        );
    }

    private void stubCustomer(boolean customerExists)
    {
        if (customerExists)
        {
            Customer customer = new Customer();
            customer.setId(CUSTOMER_ID);

            when(customerRepository.findById(CUSTOMER_ID, Customer.class))
                    .thenReturn(customer);
        }
        else
        {
            when(customerRepository.findById(CUSTOMER_ID, Customer.class))
                    .thenReturn(null);
        }
    }

    private void stubSubHandler(boolean subHandlerExists, double basePrice, boolean customerExists)
    {
        if (!customerExists)
        {
            return;
        }

        if (subHandlerExists)
        {
            SubHandler subHandler = new SubHandler();
            subHandler.setId(SUB_HANDLER_ID);
            subHandler.setBasePrice(basePrice);

            when(subHandlerService.findSubHandlerById(SUB_HANDLER_ID))
                    .thenReturn(subHandler);
        }
        else
        {
            when(subHandlerService.findSubHandlerById(SUB_HANDLER_ID))
                    .thenReturn(null);
        }
    }


    private static Stream<Arguments> validateCustomerCases()
    {
        return Stream.of(

                Arguments.of(
                        "unique phone, strong password",
                        true,
                        "ab12CD34",
                        true,
                        null
                ),

                Arguments.of(
                        "duplicate phone, strong password",
                        false,
                        "ab12CD34",
                        false,
                        null
                ),

                Arguments.of(
                        "unique phone, short password",
                        true,
                        "aB12",
                        null,
                        PassNot8Digits.class
                ),

                Arguments.of(
                        "unique phone, only letters",
                        true,
                        "abcdefgh",
                        null,
                        AllNotBeLetterOrDigits.class
                ),

                Arguments.of(
                        "unique phone, only digits",
                        true,
                        "12345678",
                        null,
                        AllNotBeLetterOrDigits.class
                ),

                Arguments.of(
                        "duplicate phone, invalid password but short-circuited",
                        false,
                        "123",
                        false,
                        null
                )
        );
    }


    @ParameterizedTest(name = "{0}")
    @MethodSource("validateCustomerCases")
    void validateCustomer_shouldBehaveCorrectly(
            String caseName,
            boolean repoReturnsNull,           // true -> not duplicate, false -> duplicate
            String password,
            Boolean expectedResult,            // null if expecting exception
            Class<? extends Throwable> expectedException
    )
    {
        String phone = "09120000000";


        if (repoReturnsNull)
        {
            when(customerRepository.find(phone, Customer.class))
                    .thenReturn(null); // not duplicate
        }
        else
        {
            Customer existing = new Customer();
            existing.setId(100);
            when(customerRepository.find(phone, Customer.class))
                    .thenReturn(existing); // duplicate
        }


        CustomerSignUpDto dto = new CustomerSignUpDto(
                "Ali",
                "Ahmadi",
                "a@test.com",
                phone,
                password
        );

        if (expectedException != null)
        {
            assertThrows(
                    expectedException,
                    () -> customerService.validateCustomer(dto)
            );
        }
        else
        {
            boolean result = customerService.validateCustomer(dto);
            assertEquals(expectedResult, result);
        }
    }

    private static Stream<Arguments> addCommentInvalidCases()
    {
        return Stream.of(
                Arguments.of(
                        "existing score already set",
                        3,
                        OrderState.PAID,
                        4,
                        DuplicateCommentException.class
                ),
                Arguments.of(
                        "order state not PAID",
                        null,
                        OrderState.STARTED,
                        4,
                        OrderStateIsNotCorrect.class
                ),
                Arguments.of(
                        "star below 0",
                        null,
                        OrderState.PAID,
                        -1,
                        StarShouldBeenBetween1To5.class
                ),
                Arguments.of(
                        "star above 5",
                        null,
                        OrderState.PAID,
                        6,
                        StarShouldBeenBetween1To5.class
                )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("addCommentInvalidCases")
    void addComment_invalidCases_shouldThrow(
            String caseName,
            Integer existingScore,
            OrderState orderState,
            int star,
            Class<? extends Throwable> expectedException
    )
    {
        Integer customerId = 1;
        Integer orderId = 10;

        Authentication auth =
                new UsernamePasswordAuthenticationToken(customerId, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Customer customer = new Customer();
        customer.setId(customerId);

        Employee employee = new Employee();
        employee.setId(5);
        employee.setScore(10);

        Orders order = new Orders();
        order.setId(orderId);
        order.setCustomer(customer);
        order.setEmployee(employee);
        order.setOrderState(orderState);
        order.setScore(existingScore);

        when(orderService.findById(orderId)).thenReturn(order);

        assertThrows(
                expectedException,
                () -> customerService.addComment(orderId, star, "some comment")
        );

        verify(orderService, never()).update(any());
    }

    @Test
    void addComment_shouldSetCommentAndScoreAndUpdateEmployee_whenValid()
    {
        Integer customerId = 1;
        Integer orderId = 10;
        int star = 4;

        Authentication auth =
                new UsernamePasswordAuthenticationToken(customerId, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Customer customer = new Customer();
        customer.setId(customerId);

        Employee employee = new Employee();
        employee.setId(5);
        employee.setScore(10);

        Orders order = new Orders();
        order.setId(orderId);
        order.setCustomer(customer);
        order.setEmployee(employee);
        order.setOrderState(OrderState.PAID);
        order.setScore(null);

        when(orderService.findById(orderId)).thenReturn(order);

        customerService.addComment(orderId, star, "great work");

        assertEquals("great work", order.getComment());
        assertEquals(star, order.getScore());
        assertEquals(14, employee.getScore()); // 10 + 4

        verify(orderService, times(1)).update(order);
    }

    @Test
    void addComment_shouldThrowNotFoundOrder_whenOrderDoesNotExist()
    {
        int orderId = 10;

        when(orderService.findById(orderId)).thenReturn(null);

        assertThrows(
                NotFoundOrder.class,
                () -> customerService.addComment(orderId, 3, "x")
        );

        verify(orderService, never()).update(any());
    }

    @Test
    void addComment_shouldThrowNotFoundOrder_whenOrderDoesNotBelongToCurrentCustomer()
    {
        Integer currentCustomerId = 1;
        Integer otherCustomerId = 2;
        Integer orderId = 10;

        Authentication auth =
                new UsernamePasswordAuthenticationToken(currentCustomerId, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Customer otherCustomer = new Customer();
        otherCustomer.setId(otherCustomerId);

        Orders order = new Orders();
        order.setId(orderId);
        order.setCustomer(otherCustomer);
        order.setOrderState(OrderState.PAID);

        when(orderService.findById(orderId)).thenReturn(order);

        assertThrows(
                NotFoundOrder.class,
                () -> customerService.addComment(orderId, 3, "x")
        );

        verify(orderService, never()).update(any());
    }

    private static final Integer START_ORDER_CUSTOMER_ID = 1;
    private static final Integer START_ORDER_ID = 10;

    @Test
    void startOrder_shouldSetStateToStarted_andUpdateOrder_whenValid()
    {
        Authentication auth =
                new UsernamePasswordAuthenticationToken(START_ORDER_CUSTOMER_ID, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Customer customer = new Customer();
        customer.setId(START_ORDER_CUSTOMER_ID);

        Orders order = new Orders();
        order.setId(START_ORDER_ID);
        order.setCustomer(customer);
        order.setOrderState(OrderState.UNDER_REACHING_EMPLOYEE);

        Offer offer = new Offer();
        offer.setTimeOfWork(LocalDateTime.now().minusMinutes(5));

        when(orderService.findById(START_ORDER_ID)).thenReturn(order);
        when(offerService.findAcceptedOfferInOrder(START_ORDER_ID)).thenReturn(offer);

        customerService.startOrder(START_ORDER_ID);

        assertEquals(OrderState.STARTED, order.getOrderState());
        verify(orderService, times(1)).update(order);
    }

    private static Stream<Arguments> startOrderInvalidCases()
    {
        return Stream.of(
                // 1) order not found
                Arguments.of(
                        "order not found",
                        false,
                        true,
                        true,
                        true,
                        false,
                        NotFoundOrder.class
                ),
                // 2) different customer
                Arguments.of(
                        "different customer",
                        true,
                        false,
                        true,
                        true,
                        false,
                        NotFoundOrder.class
                ),
                // 3) wrong state
                Arguments.of(
                        "wrong state (not UNDER_REACHING_EMPLOYEE)",
                        true,
                        true,
                        false,
                        true,
                        false,
                        OrderStateIsNotCorrect.class
                ),

                Arguments.of(
                        "accepted offer not found",
                        true,
                        true,
                        true,
                        false,     // offerExists
                        false,
                        NotFoundOffer.class
                ),

                Arguments.of(
                        "offer time after now",
                        true,
                        true,
                        true,
                        true,
                        true,      // offerTimeAfterNow
                        TimeOfWorkDoesntMatch.class
                )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("startOrderInvalidCases")
    void startOrder_invalidCases_shouldThrow(
            String caseName,
            boolean orderExists,
            boolean sameCustomer,
            boolean correctState,
            boolean offerExists,
            boolean offerTimeAfterNow,
            Class<? extends Throwable> expectedException
    )
    {
        Authentication auth =
                new UsernamePasswordAuthenticationToken(START_ORDER_CUSTOMER_ID, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        if (!orderExists)
        {
            when(orderService.findById(START_ORDER_ID)).thenReturn(null);
        }
        else
        {
            Orders order = buildOrderForStart(sameCustomer, correctState);
            when(orderService.findById(START_ORDER_ID)).thenReturn(order);


            if (sameCustomer && correctState)
            {
                stubOfferForStart(offerExists, offerTimeAfterNow);
            }
        }

        assertThrows(
                expectedException,
                () -> customerService.startOrder(START_ORDER_ID)
        );

        verify(orderService, never()).update(any());
    }

    private Orders buildOrderForStart(boolean sameCustomer, boolean correctState)
    {
        Customer customer = new Customer();
        customer.setId(sameCustomer ? START_ORDER_CUSTOMER_ID : 999);

        Orders order = new Orders();
        order.setId(START_ORDER_ID);
        order.setCustomer(customer);
        order.setOrderState(
                correctState ? OrderState.UNDER_REACHING_EMPLOYEE
                        : OrderState.WAITING_FOR_EMPLOYEE_OFFER
        );
        return order;
    }

    private void stubOfferForStart(boolean offerExists, boolean offerTimeAfterNow)
    {
        if (!offerExists)
        {
            when(offerService.findAcceptedOfferInOrder(CustomerServiceTest.START_ORDER_ID)).thenReturn(null);
            return;
        }

        Offer offer = new Offer();
        LocalDateTime time = offerTimeAfterNow
                ? LocalDateTime.now().plusMinutes(5)
                : LocalDateTime.now().minusMinutes(5);
        offer.setTimeOfWork(time);

        when(offerService.findAcceptedOfferInOrder(CustomerServiceTest.START_ORDER_ID)).thenReturn(offer);
    }


    @Test
    void createCustomer_shouldMapAddCreditPublishEventSendTokenAndSignUp_whenValid()
    {
        // arrange
        CustomerSignUpDto dto =
                new CustomerSignUpDto(
                        "Ali",
                        "Ahmadi",
                        "a@test.com",
                        "09120000000",
                        "Ab12Cd34"
                );

        Customer mappedCustomer = new Customer();
        mappedCustomer.setId(1);

        // spy so we can stub validateCustomer + verify sendToken
        CustomerServiceImp spyService = Mockito.spy(customerService);

        doReturn(true)
                .when(spyService)
                .validateCustomer(dto);

        when(entityMapper.dtoToCustomer(dto))
                .thenReturn(mappedCustomer);

        when(passwordEncoder.encode("Ab12Cd34"))
                .thenReturn("ENC(Ab12Cd34)");


        CustomerSignUpDto result = spyService.createCustomer(dto);

        assertSame(dto, result);


        ArgumentCaptor<Customer> customerCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(baseUserRepository, times(1))
                .save(customerCaptor.capture());

        Customer saved = customerCaptor.getValue();


        assertNotNull(saved.getTimeOfRegistration());

        assertNotNull(saved.getCredit());
        assertEquals(0d, saved.getCredit().getAmount());
        assertEquals(TypeOfUser.CUSTOMER, saved.getCredit().getTypeOfEmployee());

        assertNotNull(saved.getPassAndUser());
        assertEquals(dto.phone(), saved.getPassAndUser().getUsername());
        assertEquals("ENC(Ab12Cd34)", saved.getPassAndUser().getPass());
        assertEquals(TypeOfUser.CUSTOMER, saved.getPassAndUser().getTypeOfUser());


        verify(entityMapper, times(1))
                .dtoToCustomer(dto);


        verify(publisher, times(1))
                .publishEvent(any(UserCreationEvent.class));


        verify(spyService, times(1))
                .sendToken(dto.email(), TypeOfUser.CUSTOMER);
    }

    @Test
    void createCustomer_shouldThrowInvalidCustomerDataException_whenValidationFails()
    {
        CustomerSignUpDto dto =
                new CustomerSignUpDto(
                        "Ali",
                        "Ahmadi",
                        "a@test.com",
                        "09120000000",
                        "badPass"
                );

        CustomerServiceImp spyService = Mockito.spy(customerService);

        // force validation to fail
        doReturn(false)
                .when(spyService)
                .validateCustomer(dto);

        assertThrows(
                InvalidCustomerDataException.class,
                () -> spyService.createCustomer(dto)
        );

        // ensure nothing else is called
        verify(entityMapper, never()).dtoToCustomer(any());
        verify(baseUserRepository, never()).save(any());
        verify(publisher, never()).publishEvent(any());
        verify(spyService, never()).sendToken(anyString(), any());
    }

    @Test
    void findDoneWorksById_shouldThrowNotFoundOrder_whenNoPaidOrders()
    {
        Integer customerId = 1;

        when(orderService.findPaidOrdersForCustomer(customerId))
                .thenReturn(null);

        assertThrows(
                NotFoundOrder.class,
                () -> customerService.findDoneWorksById(customerId)
        );

        verify(offerService, never()).findAcceptedOfferInOrder(anyInt());
        verify(customerRepository, never()).findById(anyInt(), eq(Customer.class));
    }

    @Test
    void findDoneWorksById_shouldReturnEmptyList_whenPaidOrdersHaveNoOffers()
    {
        Integer customerId = 1;

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("Ali");
        customer.setLast_name("Ahmadi");

        SubHandler subHandler = new SubHandler();
        subHandler.setName("Cleaning");
        subHandler.setDetail("Home cleaning");
        subHandler.setBasePrice(100.0);

        Orders order1 = new Orders();
        order1.setId(10);
        order1.setCustomer(customer);
        order1.setSubHandler(subHandler);
        order1.setTimeOfWork(LocalDateTime.now());
        order1.setScore(5);
        order1.setComment("Good");

        Orders order2 = new Orders();
        order2.setId(11);
        order2.setCustomer(customer);
        order2.setSubHandler(subHandler);
        order2.setTimeOfWork(LocalDateTime.now());
        order2.setScore(4);
        order2.setComment("Nice");

        when(orderService.findPaidOrdersForCustomer(customerId))
                .thenReturn(List.of(order1, order2));


        when(offerService.findAcceptedOfferInOrder(10)).thenReturn(null);
        when(offerService.findAcceptedOfferInOrder(11)).thenReturn(null);

        List<DoneDutiesDto> result = customerService.findDoneWorksById(customerId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(offerService, times(1)).findAcceptedOfferInOrder(10);
        verify(offerService, times(1)).findAcceptedOfferInOrder(11);
        verify(customerRepository, never()).findById(anyInt(), eq(Customer.class));
    }


    @Test
    void findDoneWorksById_shouldReturnDoneDutiesDtos_whenOffersExist()
    {
        Integer customerId = 1;

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("Ali");
        customer.setLast_name("Ahmadi");

        SubHandler subHandler = new SubHandler();
        subHandler.setName("Cleaning");
        subHandler.setDetail("Home cleaning");
        subHandler.setBasePrice(200.0);

        LocalDateTime time1 = LocalDateTime.now().minusDays(1);
        LocalDateTime time2 = LocalDateTime.now().minusDays(2);

        Orders order1 = new Orders();
        order1.setId(10);
        order1.setCustomer(customer);
        order1.setSubHandler(subHandler);
        order1.setTimeOfWork(time1);
        order1.setScore(5);
        order1.setComment("Great job");

        Orders order2 = new Orders();
        order2.setId(11);
        order2.setCustomer(customer);
        order2.setSubHandler(subHandler);
        order2.setTimeOfWork(time2);
        order2.setScore(null);
        order2.setComment("Ok");

        Employee emp1 = new Employee();
        emp1.setId(100);
        emp1.setName("Reza");
        emp1.setLast_name("Rezayi");

        Employee emp2 = new Employee();
        emp2.setId(200);
        emp2.setName("Sara");
        emp2.setLast_name("Mohammadi");

        Offer offer1 = new Offer();
        offer1.setEmployee(emp1);
        offer1.setOfferPrice(500L);

        Offer offer2 = new Offer();
        offer2.setEmployee(emp2);
        offer2.setOfferPrice(300L);

        when(orderService.findPaidOrdersForCustomer(customerId))
                .thenReturn(List.of(order1, order2));

        when(offerService.findAcceptedOfferInOrder(10)).thenReturn(offer1);
        when(offerService.findAcceptedOfferInOrder(11)).thenReturn(offer2);

        when(customerRepository.findById(customerId, Customer.class))
                .thenReturn(customer);

        List<DoneDutiesDto> result = customerService.findDoneWorksById(customerId);

        assertEquals(2, result.size());

        DoneDutiesDto dto1 = result.get(0);
        assertEquals(time1, dto1.timeOfDuty());
        assertEquals(500.0, dto1.price());
        assertEquals("Cleaning", dto1.subHandler().name());
        assertEquals(5, dto1.employeeScore());
        assertEquals(100, dto1.employeeId());
        assertEquals("Reza Rezayi", dto1.employeeFullName());
        assertEquals("Ali Ahmadi", dto1.customerFullName());
        assertEquals(customerId, dto1.customerId());
        assertEquals("Great job", dto1.comment());

        DoneDutiesDto dto2 = result.get(1);
        assertEquals(time2, dto2.timeOfDuty());
        assertEquals(300.0, dto2.price());
        assertEquals(0, dto2.employeeScore());
        assertEquals(200, dto2.employeeId());
        assertEquals("Sara Mohammadi", dto2.employeeFullName());

        verify(orderService, times(1)).findPaidOrdersForCustomer(customerId);
        verify(offerService, times(1)).findAcceptedOfferInOrder(10);
        verify(offerService, times(1)).findAcceptedOfferInOrder(11);
        verify(customerRepository, times(2))
                .findById(customerId, Customer.class);
    }
}

