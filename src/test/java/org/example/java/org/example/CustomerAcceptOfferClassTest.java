//package org.example.java.org.example;
//
//import org.example.domain.*;
//import org.example.dto.OfferDto;
//import org.example.dto.OrderDto;
//import org.example.enumirations.OrderState;
//import org.example.exeptions.*;
//import org.example.service.mainService.imp.CustomerAcceptOfferClass;
//import org.example.service.offer.OfferService;
//import org.example.service.order.OrderService;
//import org.example.service.subHandler.SubHandlerService;
//import org.example.service.user.customer.CustomerService;
//import org.example.service.user.employee.EmployeeService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//public class CustomerAcceptOfferClassTest {
//    @Mock
//    private OrderService orderService;
//
//    @Mock
//    private SubHandlerService subHandlerService;
//
//    @Mock
//    private OfferService offerService;
//
//    @Mock
//    private EmployeeService employeeService;
//
//    @Mock
//    private CustomerService customerService;
//
//
//
//    @InjectMocks
//    private CustomerAcceptOfferClass customerAcceptOfferClass;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//    @Test
//    void testGetSubHandlerForCustomer_success() throws Exception {
//        OrderDto orderDto = new OrderDto(60.0,"want to fix my pipe",
//                LocalDateTime.now().plusDays(1),"mir",1,1);
//        Customer customer = new Customer();
//        SubHandler subHandler = new SubHandler();
//        subHandler.setBasePrice(50.0);
//        when(customerService.findById(orderDto.customerId(), Customer.class)).thenReturn(customer);
//        when(subHandlerService.findSubHandlerById(orderDto.subHandlerId())).thenReturn(subHandler);
//        customerAcceptOfferClass.GetSubHandlerForCustomer(orderDto);
//        verify(orderService).save(any(Orders.class));
//    }
//
//    @Test
//    void testGetSubHandlerForCustomer_subHandlerNotFound() {
//        OrderDto orderDto = new OrderDto(10.0,"want to fix my pipe",
//                LocalDateTime.now().plusDays(1),"mir",1,1);
//        when(customerService.findById(orderDto.customerId(), Customer.class)).thenReturn(new Customer());
//        when(subHandlerService.findSubHandlerById(orderDto.subHandlerId())).thenReturn(null);
//        assertThrows(HandlerIsNull.class, () -> customerAcceptOfferClass.GetSubHandlerForCustomer(orderDto));
//    }
//
//    @Test
//    void testGetSubHandlerForCustomer_customerNotFound() {
//        OrderDto orderDto = new OrderDto(10.0,"want to fix my pipe",
//                LocalDateTime.now().plusDays(1),"mir",1,1);
//        when(customerService.findById(orderDto.customerId(), Customer.class)).thenReturn(null);
//        assertThrows(NotFoundCustomer.class, () -> customerAcceptOfferClass.GetSubHandlerForCustomer(orderDto));
//    }
//
//    @Test
//    void testGetSubHandlerForCustomer_timeOfWorkDoesNotMatch() {
//        OrderDto orderDto = new OrderDto(20000.0,"want to fix my pipe",
//                LocalDateTime.now().minusDays(2),"mir",1,1);
//        Customer customer = new Customer();
//        SubHandler subHandler = new SubHandler();
//        subHandler.setBasePrice(50.0);
//        when(customerService.findById(orderDto.customerId(), Customer.class)).thenReturn(customer);
//        when(subHandlerService.findSubHandlerById(orderDto.subHandlerId())).thenReturn(subHandler);
//        assertThrows(TimeOfWorkDoesntMatch.class, () -> customerAcceptOfferClass.GetSubHandlerForCustomer(orderDto));
//    }
//
//    @Test
//    void testGetSubHandlerForCustomer_orderPriceTooLow() {
//
//        OrderDto orderDto = new OrderDto(10.0,"want to fix my pipe",
//                LocalDateTime.now().plusDays(1),"mir",1,1);
//        Customer customer = new Customer();
//        SubHandler subHandler = new SubHandler();
//        subHandler.setBasePrice(50.0);
//        when(customerService.findById(orderDto.customerId(), Customer.class)).thenReturn(customer);
//        when(subHandlerService.findSubHandlerById(orderDto.subHandlerId())).thenReturn(subHandler);
//        assertThrows(OrderPriceShouldBeHigherThanBase.class, () -> customerAcceptOfferClass.GetSubHandlerForCustomer(orderDto));
//    }
//
//    @Test
//    void testGiveOfferToOrder_success() throws Exception {
//
//        OfferDto offerDto = new OfferDto(20000L,LocalDateTime.now().plusDays(3),40,1,1);
//        Orders order = new Orders();
//        order.setOrderState(OrderState.WAITING_FOR_EMPLOYEE_OFFER);
//        order.setOfferedPrice(100.0);
//        order.setTimeOfWork(LocalDateTime.now().plusDays(2)); // Future date
//        Employee employee = new Employee();
//        when(employeeService.findById(1, Employee.class)).thenReturn(employee);
//        when(orderService.findById(1)).thenReturn(order);
//        customerAcceptOfferClass.GiveOfferToOrder(offerDto);
//        verify(orderService).update(order);
//        verify(offerService).save(any(Offer.class));
//    }
//    @Test
//    void testGiveOfferToOrder_offerPriceLessThanOrderPrice() {
//
//        OfferDto offerDto = new OfferDto(10L,LocalDateTime.now().plusDays(3),40,1,1);
//
//        Orders order = new Orders();
//        order.setOrderState(OrderState.WAITING_FOR_EMPLOYEE_OFFER);
//        order.setOfferedPrice(100.0);
//        order.setTimeOfWork(LocalDateTime.now().plusDays(2));
//
//        Employee employee = new Employee();
//
//        when(employeeService.findById(1, Employee.class)).thenReturn(employee);
//        when(orderService.findById(1)).thenReturn(order);
//
//
//        assertThrows(OfferPriceIsLessThanOrderPrice.class, () -> customerAcceptOfferClass.GiveOfferToOrder(offerDto));
//    }
//
//    @Test
//    void testGiveOfferToOrder_timeOfWorkDoesNotMatch() {
//
//      OfferDto offerDto = new OfferDto(200L,LocalDateTime.now().plusDays(1),40,1,1);
//        Orders order = new Orders();
//        order.setOrderState(OrderState.WAITING_FOR_EMPLOYEE_OFFER);
//        order.setOfferedPrice(100.0);
//        order.setTimeOfWork(LocalDateTime.now().plusDays(2));
//        Employee employee = new Employee();
//        when(employeeService.findById(1, Employee.class)).thenReturn(employee);
//        when(orderService.findById(1)).thenReturn(order);
//        assertThrows(TimeOfWorkDoesntMatch.class, () -> customerAcceptOfferClass.GiveOfferToOrder(offerDto));
//    }
//
//    @Test
//    void testGiveOfferToOrder_invalidOrderState() {
//        OfferDto offerDto = new OfferDto(200L,LocalDateTime.now().plusDays(3),40,1,1);
//        Orders order = new Orders();
//        order.setOrderState(OrderState.DONE);
//        order.setOfferedPrice(100.0);
//        order.setTimeOfWork(LocalDateTime.now().plusDays(2));
//        Employee employee = new Employee();
//        when(employeeService.findById(1, Employee.class)).thenReturn(employee);
//        when(orderService.findById(1)).thenReturn(order);
//        assertThrows(OrderStateIsNotCorrect.class, () -> customerAcceptOfferClass.GiveOfferToOrder(offerDto));
//    }
//
//    @Test
//    void testCustomerAcceptOffer_success() throws Exception {
//        Integer offerId = 1;
//        Offer offer = new Offer();
//        offer.setId(offerId);
//        offer.setAccepted(false);
//        Orders orders = new Orders();
//        orders.setId(1);
//        Employee employee = new Employee();
//        employee.setId(1);
//        offer.setOrders(orders);
//        offer.setEmployee(employee);
//        when(offerService.findById(offerId)).thenReturn(offer);
//        when(orderService.findById(orders.getId())).thenReturn(orders);
//        when(employeeService.findById(employee.getId(), Employee.class)).thenReturn(employee);
//        customerAcceptOfferClass.customerAcceptOffer(offerId);
//        verify(offerService).update(offer);
//        verify(orderService).update(orders);
//        verify(employeeService).findById(employee.getId(), Employee.class);
//    }
//
//    @Test
//    void testCustomerAcceptOffer_offerNotFound() {
//        Integer offerId = 1;
//        when(offerService.findById(offerId)).thenReturn(null);
//        assertThrows(NotFoundOffer.class, () -> customerAcceptOfferClass.customerAcceptOffer(offerId));
//    }
//
//
//    @Test
//    void testSetOrderStateToStart_success() throws Exception {
//
//        Integer orderId = 1;
//        Orders orders = new Orders();
//        orders.setId(orderId);
//        orders.setOrderState(OrderState.WAITING_FOR_EMPLOYEE_OFFER);
//
//        when(orderService.findById(orderId)).thenReturn(orders);
//
//        customerAcceptOfferClass.setOrderStateToStart(orderId, OrderState.UNDER_REACHING_EMPLOYEE);
//
//
//        verify(orderService).update(orders);
//    }
//
//    @Test
//    void testSetOrderStateToStart_orderNotFound() {
//        Integer orderId = 1;
//        when(orderService.findById(orderId)).thenReturn(null);
//
//        assertThrows(NotFoundOrder.class, () -> customerAcceptOfferClass.setOrderStateToStart(orderId, OrderState.UNDER_REACHING_EMPLOYEE));
//    }
//}
//
