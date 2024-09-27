package org.example.service.getSubHandlerForCustomer;

import org.example.domain.Customer;
import org.example.domain.Offer;
import org.example.domain.Orders;

public interface PaymentService {
    void customerChargeCart(Customer customer, double amount);
    void customerPayOrder(Integer ordersId, Customer customer);
    boolean validateCustomerCanPay(Orders orders, Customer customer, Offer offer);
}
