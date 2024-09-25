package org.example.service.getSubHandlerForCustomer.imp;

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
import org.example.service.user.employee.EmployeeService;
import org.example.service.user.employee.imp.EmployeeServiceImp;

import java.time.LocalDateTime;
import java.util.Objects;

public class GetSubHandlerForCustomerService {
    private final OrderService orderService;
    private final SubHandlerService subHandlerService;
    private final OfferService offerService;
    private final EmployeeService employeeService;
    public  GetSubHandlerForCustomerService(){
        orderService = new OrderServiceImp();
        subHandlerService = new SubHandlerServiceImp();
        offerService = new OfferServiceImp();
        employeeService = new EmployeeServiceImp();
    }
    @SneakyThrows
    public void GetSubHandlerForCustomerService(Integer subHandlerId, Customer customer, OrderDto orderDto) {
        try {
            SubHandler subHandler =  subHandlerService.findSubHandlerById(subHandlerId);
            if (Objects.isNull(subHandler)) {
                throw new HandlerIsNull();
            }
            Order order = new Order();
            order.setCustomer(customer);
            order.setSubHandler(subHandler);
            order.setAddress(orderDto.address());
            order.setDetail(orderDto.detail());
            order.setOrderState(OrderState.WAITING_FOR_EMPLOYEE_OFFER);
            order.setOfferedPrice(orderDto.OfferedPrice());
            orderService.save(order);
        }catch (Exception e){
            throw new Exception(e);
        }
    }
    @SneakyThrows
    public void GiveOfferToOrder(Integer orderId, Employee employee, OfferDto offerDto){
        try {
            Order order = orderService.findById(orderId);
            if (validateIfItCanGetOffer(order)) {
                Offer offer = new Offer();
                offer.setEmployee(employee);
                offer.setTimeOfCreate(LocalDateTime.now());
                offer.setOfferPrice(offer.getOfferPrice());
                if (offerDto.timeOfWork().isAfter(order.getTimeOfWork())) {
                    offer.setTimeOfWork(offerDto.timeOfWork());
                } else {
                    throw new TimeOfWorkDoesntMatch();
                }
                offer.setWorkTimeInMinutes(offerDto.WorkTimeInMinutes());
                order.setOrderState(OrderState.UNDER_CHOOSING_EMPLOYEE);
                orderService.update(order);
                offer.setOrder(order);
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
            Order order = orderService.findById(offer.getOrder().getId());
            Employee employee = employeeService.findById(offer.getEmployee().getId());
            order.setEmployee(employee);
            order.setOrderState(OrderState.UNDER_REACHING_EMPLOYEE);
            orderService.update(order);
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    //todo:forEmployee
    @SneakyThrows
    public void setOrderStateToStart(Integer orderId , OrderState orderState){
        try {
            Order order = orderService.findById(orderId);
            if (Objects.isNull(order)) {
                throw new NotFoundOrder();
            }
            order.setOrderState(orderState);
            orderService.update(order);
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    public void addToCreditToCustomer(){

    }
    @SneakyThrows
    public boolean validateIfItCanGetOffer(Order order){
        if (order.getOrderState().equals(OrderState.WAITING_FOR_EMPLOYEE_OFFER) &&
                order.getOrderState().equals(OrderState.UNDER_CHOOSING_EMPLOYEE)
        ) {
            return true;
        }else {
            throw new OrderStateIsNotCorrect();
        }
    }
}
