package org.example.service.mainService.imp;

import lombok.SneakyThrows;
import org.example.domain.Employee;
import org.example.domain.Offer;
import org.example.domain.Orders;
import org.example.enumirations.OrderState;
import org.example.exeptions.NotFoundException.NotFoundOffer;
import org.example.service.offer.OfferService;
import org.example.service.order.OrderService;
import org.example.service.subHandler.SubHandlerService;
import org.example.service.user.employee.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerAcceptOfferClass {
    private final OrderService orderService;
    private final SubHandlerService subHandlerService;
    private final OfferService offerService;
    private final EmployeeService employeeService;
//    private final CustomerService customerService;
    public CustomerAcceptOfferClass(OrderService orderService,
                                    SubHandlerService subHandlerService,
                                    OfferService offerService,
                                    EmployeeService employeeService
 ){
        this.orderService =  orderService;
        this.subHandlerService =  subHandlerService;
        this.offerService =  offerService;
        this.employeeService =  employeeService;
    }
//    @SneakyThrows
//    public OrderDto GetSubHandlerForCustomer(OrderDto orderDto) {
//            Customer customer = customerService.findById(orderDto.customerId() , Customer.class);
//            if (Objects.isNull(customer)) {
//                throw new NotFoundCustomer();
//            }
//            SubHandler subHandler =  subHandlerService.findSubHandlerById(orderDto.subHandlerId());
//            if (Objects.isNull(subHandler)) {
//                throw new HandlerIsNull();
//            }
//            if (isOrderValidated(orderDto,subHandler)) {
//                Orders orders = new Orders();
//                orders.setCustomer(customer);
//                orders.setSubHandler(subHandler);
//                orders.setAddress(orderDto.address());
//                orders.setDetail(orderDto.detail());
//                orders.setOrderState(OrderState.WAITING_FOR_EMPLOYEE_OFFER);
//                orders.setOfferedPrice(orderDto.offeredPrice());
//                orderService.save(orders);
//                return orderDto;
//            }
//            return null;
//    }
//    private boolean isOrderValidated(OrderDto orderDto,SubHandler subHandler)
//            throws TimeOfWorkDoesntMatch, OrderPriceShouldBeHigherThanBase {
//        if (orderDto.timeOfWork().isBefore(LocalDateTime.now())) {
//            throw new TimeOfWorkDoesntMatch();
//        }
//        if (orderDto.offeredPrice() < subHandler.getBasePrice()) {
//            throw new OrderPriceShouldBeHigherThanBase();
//        }
//        return true;
//    }
    //employee
//@SneakyThrows
//    public OfferDto GiveOfferToOrder(OfferDto offerDto)
//    {
//            Employee employee = employeeService.findById(offerDto.employeeId(),Employee.class);
//            Orders orders = orderService.findById(offerDto.orderId());
//            if (validateIfItCanGetOffer(orders)) {
//                Offer offer = new Offer();
//                offer.setEmployee(employee);
//                offer.setTimeOfCreate(LocalDateTime.now());
//                if (offerDto.offerPrice()<orders.getOfferedPrice()){
//                    throw new OfferPriceIsLessThanOrderPrice();
//                }
//                offer.setOfferPrice(offerDto.offerPrice());
//                if (offerDto.timeOfWork().isAfter(orders.getTimeOfWork())) {
//                    offer.setTimeOfWork(offerDto.timeOfWork());
//                } else {
//                    throw new TimeOfWorkDoesntMatch();
//                }
//                offer.setWorkTimeInMinutes(offerDto.workTimeInMinutes());
//                orders.setOrderState(OrderState.UNDER_CHOOSING_EMPLOYEE);
//                orderService.update(orders);
//                offer.setOrders(orders);
//                offerService.save(offer);
//                return offerDto;
//            }
//            return null;
//    }
    @SneakyThrows
    @Transactional
    public void customerAcceptOffer(Integer  offerId){
        try {
            Offer offer = offerService.findById(offerId);
            if (offer == null) {
                throw new NotFoundOffer();
            }
            offer.setAccepted(true);
            offerService.update(offer);
            Orders orders = orderService.findById(offer.getOrders().getId());
            Employee employee = employeeService.findById(offer.getEmployee().getId(),Employee.class);
            orders.setEmployee(employee);
            orders.setOrderState(OrderState.UNDER_REACHING_EMPLOYEE);
            orderService.update(orders);
        }catch (Exception e){
            throw e;
        }
    }

//    //todo:forEmployee
//    @SneakyThrows
//    public void setOrderStateToStart(Integer orderId , OrderState orderState){
//        try {
//            Orders orders = orderService.findById(orderId);
//            if (Objects.isNull(orders)) {
//                throw new NotFoundOrder();
//            }
//            orders.setOrderState(orderState);
//            orderService.update(orders);
//        }catch (Exception e){
//            throw e;
//        }
//    }
//    @SneakyThrows
//    public boolean validateIfItCanGetOffer(Orders orders){
//        if (orders.getOrderState().equals(OrderState.WAITING_FOR_EMPLOYEE_OFFER) ||
//                orders.getOrderState().equals(OrderState.UNDER_CHOOSING_EMPLOYEE)
//        ) {
//            return true;
//        }else {
//            throw new OrderStateIsNotCorrect();
//        }
//    }
//

}
