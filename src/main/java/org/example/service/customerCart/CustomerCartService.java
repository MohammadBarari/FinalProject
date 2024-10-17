package org.example.service.customerCart;

import jakarta.transaction.Transactional;
import org.example.domain.CustomerCart;
import org.example.exeptions.WrongEntrance;
import org.springframework.stereotype.Service;

@Service
public interface CustomerCartService {

    CustomerCart saveCustomerCart(CustomerCart customerCart) throws WrongEntrance;
    CustomerCart updateCustomerCart(CustomerCart customerCart);
    CustomerCart getCustomerCart(Integer id);
    CustomerCart findCustomerCartByCustomerId(Integer customerId);

}
