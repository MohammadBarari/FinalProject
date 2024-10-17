package org.example.repository.customerCart;

import org.example.domain.CustomerCart;

public interface CustomerCartRepository {
    void saveCustomerCart(CustomerCart customerCart);
    void updateCustomerCart(CustomerCart customerCart);
    CustomerCart selectCustomerCart(Integer id);
    CustomerCart selectByCustomerId(Integer customerId);
}
