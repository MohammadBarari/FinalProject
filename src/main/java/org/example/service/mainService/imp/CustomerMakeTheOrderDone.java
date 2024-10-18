package org.example.service.mainService.imp;

import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.example.domain.Employee;
import org.example.domain.Offer;
import org.example.domain.Orders;
import org.example.enumirations.EmployeeState;
import org.example.enumirations.OrderState;
import org.example.exeptions.*;
import org.example.service.offer.OfferService;
import org.example.service.order.OrderService;
import org.example.service.user.employee.EmployeeService;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;

@Service
public class CustomerMakeTheOrderDone {
    private final OrderService orderService;
    private final EmployeeService employeeService;
    private final OfferService offerService;

    public CustomerMakeTheOrderDone(OrderService orderService, EmployeeService employeeService, OfferService offerService) {
        this.orderService = orderService;
        this.employeeService = employeeService;
        this.offerService = offerService;
    }

    @Transactional
    public void addStarToEmployee(Integer employeeId, Integer starId) {
        Employee employee = employeeService.findById(employeeId, Employee.class);
        employee.setScore(employee.getScore() + starId);
        employeeService.updateUser(employee);
    }



    @Transactional
    @SneakyThrows
    public void makeTheOrderDone(Integer orderId)  {
        Offer offer = offerService.findAcceptedOfferInOrder(orderId);
        if (offer == null) {
            throw new NotFoundOffer();
        }
        Orders order = orderService.findById(orderId);
        if (order == null) {
            throw new NotFoundOrder();
        }
        if (order.getOrderState() != OrderState.STARTED){
            throw new OrderStateIsNotCorrect();
        }
        if (!LocalDateTime.now().isAfter(offer.getTimeOfWork())){
            throw new ItIsNotProperTimeToSetThis();
        }
        long hoursBetween = ChronoUnit.HOURS.between(offer.getTimeOfWork(), LocalDateTime.now());
        Employee employee = offerService.findEmployeeByOfferId(offer.getId());
        if (employee == null) {
            throw new NotFoundEmployee();
        }
        Long scoreDecreasing = offer.getWorkTimeInMinutes() / 60 - hoursBetween;
        if (scoreDecreasing < 0) {
            if ((employee.getScore() + scoreDecreasing) <0){
            employee.setScore(0);
            employee.setEmployeeState(EmployeeState.DISABLED);
            }
        }
            employeeService.updateUser(employee);

        order.setOrderState(OrderState.DONE);
        orderService.update(order);
    }
}
