package org.example.service.mainService.combinedClassesService;

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
}
