package org.example.service.customerCart;

import org.example.domain.CustomerCart;
import org.springframework.stereotype.Service;

@Service
public interface CustomerCartService {

    void updateCustomerCart(CustomerCart customerCart);

    CustomerCart findCustomerCartByCustomerId(Integer customerId);

}
