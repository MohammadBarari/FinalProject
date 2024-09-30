package org.example.service.mainService.imp;

import lombok.SneakyThrows;
import org.example.domain.*;
import org.example.dto.OfferDto;
import org.example.dto.OrderDto;
import org.example.enumirations.OrderState;
import org.example.exeptions.*;
import org.example.service.offer.OfferService;
import org.example.service.offer.imp.OfferServiceImp;
import org.example.service.order.OrderService;
import org.example.service.order.imp.OrderServiceImp;
import org.example.service.subHandler.SubHandlerService;
import org.example.service.subHandler.imp.SubHandlerServiceImp;
import org.example.service.user.customer.CustomerService;
import org.example.service.user.customer.imp.CustomerServiceImp;
import org.example.service.user.employee.EmployeeService;
import org.example.service.user.employee.imp.EmployeeServiceImp;

import java.time.LocalDateTime;
import java.util.Objects;

public class GetSubHandlerForCustomerService {
    private final OrderService orderService;
    private final SubHandlerService subHandlerService;
    private final OfferService offerService;
    private final EmployeeService employeeService;
    private final CustomerService customerService;
    public  GetSubHandlerForCustomerService(){
        orderService = new OrderServiceImp();
        subHandlerService = new SubHandlerServiceImp();
        offerService = new OfferServiceImp();
        employeeService = new EmployeeServiceImp();
        customerService = new CustomerServiceImp();
    }
    @SneakyThrows
    public void GetSubHandlerForCustomer(OrderDto orderDto) {
            Customer customer = customerService.findById(orderDto.customerId() , Customer.class);
            SubHandler subHandler =  subHandlerService.findSubHandlerById(orderDto.subHandlerId());
            if (Objects.isNull(subHandler)) {
                throw new HandlerIsNull();
            }
            if (Objects.isNull(customer)) {
                throw new NotFoundCustomer();
            }
            if (isOrderValidated(orderDto,subHandler)) {
                Orders orders = new Orders();
                orders.setCustomer(customer);
                orders.setSubHandler(subHandler);
                orders.setAddress(orderDto.address());
                orders.setDetail(orderDto.detail());
                orders.setOrderState(OrderState.WAITING_FOR_EMPLOYEE_OFFER);
                orders.setOfferedPrice(orderDto.offeredPrice());
                orderService.save(orders);
            }
    }
    private boolean isOrderValidated(OrderDto orderDto,SubHandler subHandler) throws TimeOfWorkDoesntMatch, OrderPriceShouldBeHigherThanBase {
        if (orderDto.timeOfWork().isBefore(LocalDateTime.now())) {
            throw new TimeOfWorkDoesntMatch();
        }
        if (orderDto.offeredPrice() < subHandler.getBasePrice()) {
            throw new OrderPriceShouldBeHigherThanBase();
        }
        return true;
    }
    @SneakyThrows
    public void GiveOfferToOrder(OfferDto offerDto){
        try {
            Employee employee = employeeService.findById(offerDto.employeeId(),Employee.class);
            Orders orders = orderService.findById(offerDto.orderId());
            if (validateIfItCanGetOffer(orders)) {
                Offer offer = new Offer();
                offer.setEmployee(employee);
                offer.setTimeOfCreate(LocalDateTime.now());
                if (offerDto.offerPrice()<orders.getOfferedPrice()){
                    throw new OfferPriceIsLessThanOrderPrice();
                }
                offer.setOfferPrice(offerDto.offerPrice());
                if (offerDto.timeOfWork().isAfter(orders.getTimeOfWork())) {
                    offer.setTimeOfWork(offerDto.timeOfWork());
                } else {
                    throw new TimeOfWorkDoesntMatch();
                }
                offer.setWorkTimeInMinutes(offerDto.workTimeInMinutes());
                orders.setOrderState(OrderState.UNDER_CHOOSING_EMPLOYEE);
                orderService.update(orders);
                offer.setOrders(orders);
                offerService.save(offer);
            }
        }catch (Exception e){
            throw new Exception(e);
        }
    }
    @SneakyThrows
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
            throw new Exception(e);
        }
    }

    //todo:forEmployee
    @SneakyThrows
    public void setOrderStateToStart(Integer orderId , OrderState orderState){
        try {
            Orders orders = orderService.findById(orderId);
            if (Objects.isNull(orders)) {
                throw new NotFoundOrder();
            }
            orders.setOrderState(orderState);
            orderService.update(orders);
        }catch (Exception e){
            throw new Exception(e);
        }
    }
    public void addToCreditToCustomer(){

    }
    @SneakyThrows
    public boolean validateIfItCanGetOffer(Orders orders){
        if (orders.getOrderState().equals(OrderState.WAITING_FOR_EMPLOYEE_OFFER) ||
                orders.getOrderState().equals(OrderState.UNDER_CHOOSING_EMPLOYEE)
        ) {
            return true;
        }else {
            throw new OrderStateIsNotCorrect();
        }
    }
}
