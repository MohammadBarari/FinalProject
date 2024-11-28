package org.example.java.org.example;//package org.example.java.org.example;
//
//
//import org.example.domain.Offer;
//import org.example.domain.Orders;
//import org.example.enumirations.OrderState;
//import org.example.exeptions.*;
//import org.example.service.mainService.combinedClassesService.OrderAndCustomerServiceImp;
//import org.example.service.offer.OfferService;
//import org.example.service.order.OrderService;
//import org.junit.jupiter.api.BeforeEach;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.junit.jupiter.api.Test;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static junit.framework.Assert.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//public class OrderAndCustomerServiceImpTest {
//    @Mock
//    private OrderService orderService;
//
//    @Mock
//    private OfferService offerService;
//
//    @InjectMocks
//    private OrderAndCustomerServiceImp orderAndCustomerService;
//
//    private Orders order;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        order = new Orders();
//        order.setOrderState(OrderState.UNDER_REACHING_EMPLOYEE);
//    }
//
//    @Test
//    public void testChangeOrderToStart_Success() throws NotFoundOrder, OrderStateIsNotCorrect, NotFoundOrder, OrderStateIsNotCorrect {
//        Integer orderId = 1;
//        when(orderService.findById(orderId)).thenReturn(order);
//
//        orderAndCustomerService.changeOrderToStart(orderId);
//
//        assertEquals(OrderState.STARTED, order.getOrderState());
//        verify(orderService).save(order);
//    }
//
//    @Test
//    public void testChangeOrderToStart_OrderNotFound() {
//        Integer orderId = 1;
//        when(orderService.findById(orderId)).thenReturn(null);
//
//        assertThrows(NotFoundOrder.class, () -> orderAndCustomerService.changeOrderToStart(orderId));
//    }
//
//    @Test
//    public void testChangeOrderToStart_InvalidOrderState() {
//        Integer orderId = 1;
//        order.setOrderState(OrderState.STARTED);
//        when(orderService.findById(orderId)).thenReturn(order);
//
//        assertThrows(OrderStateIsNotCorrect.class, () -> orderAndCustomerService.changeOrderToStart(orderId));
//    }
//
//    @Test
//    public void testCustomerSeeAllOfferInOneOrder_Success() throws ErrorWhileFindingOffers {
//        Integer orderId = 1;
//        List<Offer> offers = Arrays.asList(new Offer(), new Offer());
//        when(offerService.findAllOffersForSpecificOrder(orderId)).thenReturn(offers);
//
//        List<Offer> result = orderAndCustomerService.customerSeeAllOfferInOneOrder(orderId);
//
//        assertEquals(offers, result);
//    }
//
//    @Test
//    public void testCustomerSeeAllOfferInOneOrder_ErrorWhileFindingOffers() {
//        Integer orderId = 1;
//        when(offerService.findAllOffersForSpecificOrder(orderId)).thenThrow(new RuntimeException());
//
//        assertThrows(ErrorWhileFindingOffers.class, () -> orderAndCustomerService.customerSeeAllOfferInOneOrder(orderId));
//    }
//}
