package org.example.service.mainService.imp;

import lombok.SneakyThrows;
import org.example.domain.*;
import org.example.enumirations.OrderState;
import org.example.exeptions.DontHaveEnoughMoney;
import org.example.exeptions.NotFoundOffer;
import org.example.service.credit.CreditService;
import org.example.service.credit.imp.CreditServiceImp;
import org.example.service.mainService.PaymentService;
import org.example.service.offer.OfferService;
import org.example.service.offer.imp.OfferServiceImp;
import org.example.service.order.OrderService;
import org.example.service.order.imp.OrderServiceImp;

public class PaymentServiceImp implements PaymentService {
    private final CreditService creditService;
    private final OfferService offerService;
    OrderService orderService;
    public PaymentServiceImp() {
        offerService = new OfferServiceImp();
        creditService = new CreditServiceImp();
        orderService = new OrderServiceImp();
    }
    @SneakyThrows
    public void customerChargeCart(Customer customer, double amount){
        try {
            Credit credit = creditService.findByCustomerId(customer.getId());
            credit.setAmount((credit.getAmount() + amount));
            creditService.update(credit);
        }catch (Exception e){
            throw new Exception(e);
        }
    }
@SneakyThrows
    public void customerPayOrder(Integer ordersId, Customer customer){
        Orders orders = orderService.findById(ordersId);
        if (orders == null){
            throw  new NotFoundOffer();
        }
        Offer offer = offerService.findAcceptedOfferInOrder(ordersId);
        //todo: validate for paying
        if (validateCustomerCanPay(orders,customer,offer)){
            Credit cusromerCredit = creditService.findCreditById(customer.getCredit().getId());
            Credit employeeCredit = creditService.findByEmployeeId(offer.getEmployee().getId());
            creditService.payToEmployee(cusromerCredit.getId(),employeeCredit.getId(),offer.getOfferPrice());
            orders.setOrderState(OrderState.PAID);
            orderService.update(orders);
        }
        //
        //
    };
    @SneakyThrows
    public boolean validateCustomerCanPay(Orders orders, Customer customer, Offer offer){
        if(customer.getCredit().getAmount() > offer.getOfferPrice() && orders.getOrderState() == OrderState.DONE){
            return true;
        }else {
            throw new DontHaveEnoughMoney();
        }
    }
}

