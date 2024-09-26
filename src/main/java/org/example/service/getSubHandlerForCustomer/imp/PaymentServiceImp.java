package org.example.service.getSubHandlerForCustomer.imp;

import lombok.SneakyThrows;
import org.example.domain.*;
import org.example.enumirations.OrderState;
import org.example.exeptions.DontHaveEnoughMoney;
import org.example.service.credit.CreditService;
import org.example.service.credit.imp.CreditServiceImp;
import org.example.service.offer.OfferService;
import org.example.service.offer.imp.OfferServiceImp;

public class PaymentServiceImp {
    private final CreditService creditService;
    private final OfferService offerService;
    public PaymentServiceImp() {
        offerService = new OfferServiceImp();
        creditService = new CreditServiceImp();
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

    public void customerPayOrder(Order order , Customer customer){
        Offer offer = offerService.findAcceptedOfferInOrder(order.getId());
        //todo: validate for paying
        if (validateCustomerCanPay(order,customer,offer)){
            Credit cusromerCredit = creditService.findCreditById(customer.getCredit().getId());
            Credit employeeCredit = creditService.findByEmployeeId(offer.getEmployee().getId());
            creditService.payToEmployee(cusromerCredit.getId(),employeeCredit.getId(),offer.getOfferPrice());
        }
        //
        //
    };
    @SneakyThrows
    public boolean validateCustomerCanPay(Order order,Customer customer,Offer offer){
        if(customer.getCredit().getAmount() > offer.getOfferPrice() && order.getOrderState() == OrderState.DONE){
            return true;
        }else {
            throw new DontHaveEnoughMoney();
        }
    }
}

