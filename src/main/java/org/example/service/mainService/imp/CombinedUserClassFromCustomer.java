package org.example.service.mainService.imp;

import jakarta.transaction.Transactional;
import org.example.domain.Employee;
import org.example.domain.Offer;
import org.example.domain.Orders;
import org.example.enumirations.EmployeeState;
import org.example.enumirations.OrderState;
import org.example.exeptions.NotFoundException.NotFoundEmployee;
import org.example.exeptions.NotFoundException.NotFoundOffer;
import org.example.exeptions.NotFoundException.NotFoundOrder;
import org.example.exeptions.order.OrderStateIsNotCorrect;
import org.example.exeptions.wrongTime.ItIsNotProperTimeToSetThis;
import org.example.service.offer.OfferService;
import org.example.service.order.OrderService;
import org.example.service.user.employee.EmployeeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class CombinedUserClassFromCustomer {
    private final OrderService orderService;
    private final EmployeeService employeeService;
    private final OfferService offerService;
    public CombinedUserClassFromCustomer(OrderService orderService, EmployeeService employeeService, OfferService offerService) {
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
    public void makeTheOrderDone(Integer orderId)  {
        Offer offer = Optional.ofNullable(offerService.findAcceptedOfferInOrder(orderId)).orElseThrow(() ->  new NotFoundOffer("unable to find offer in order with ID :" + orderId));
        Orders order = Optional.ofNullable(orderService.findById(orderId)).orElseThrow(()->new NotFoundOrder("unable to find order with ID :" + orderId));
        validateOrderDone(order, offer);
        Employee employee = Optional.ofNullable(offerService.findEmployeeByOfferId(offer.getId())).orElseThrow(() -> new NotFoundEmployee("Unable to find any employee with this offer ID : "  + offer.getId()));
        updateEmployee(offer, employee);
        order.setOrderState(OrderState.DONE);
        orderService.update(order);
    }

    private void updateEmployee(Offer offer, Employee employee) {
        long hoursBetween = ChronoUnit.HOURS.between(offer.getTimeOfWork(), LocalDateTime.now());
        Long scoreDecreasing = offer.getWorkTimeInMinutes() / 60 - hoursBetween;
        if (scoreDecreasing < 0) {
            if ((employee.getScore() + scoreDecreasing) <0){
            employee.setEmployeeState(EmployeeState.DISABLED);
            employee.setScore((int) (employee.getScore() + scoreDecreasing));
            }
        }
        employeeService.updateUser(employee);
    }
    private static void validateOrderDone(Orders order, Offer offer){
        if (order.getOrderState() != OrderState.STARTED){
            throw new OrderStateIsNotCorrect();
        }
        if (!LocalDateTime.now().isAfter(offer.getTimeOfWork())){
            throw new ItIsNotProperTimeToSetThis();
        }
    }
    @Transactional
    public void acceptOffer(Integer  offerId){
            Offer offer = Optional.ofNullable(offerService.findById(offerId)).orElseThrow(() -> new NotFoundOffer("unable to find offer with ID :" + offerId));
            Orders orders = Optional.ofNullable(orderService.findById(offer.getOrders().getId())).orElseThrow(() -> new NotFoundOrder("unable to find order with ID :" + offer.getOrders().getId()));
            offer.setAccepted(true);
            offerService.update(offer);
            Employee employee = Optional.ofNullable(employeeService.findById(offer.getEmployee().getId(),Employee.class)).orElseThrow(()-> new NotFoundEmployee("Unable to find any employee with this offer ID : "  + offerId));
            orders.setEmployee(employee);
            orders.setOrderState(OrderState.UNDER_REACHING_EMPLOYEE);
            orderService.update(orders);
    }
}
