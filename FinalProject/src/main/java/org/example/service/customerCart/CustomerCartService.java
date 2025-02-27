package org.example.service.customerCart;

import org.example.domain.CustomerCart;
import org.example.exeptions.password.PasswordOrUserNameWrong;
import org.springframework.stereotype.Service;

@Service
public interface CustomerCartService {

    CustomerCart saveCustomerCart(CustomerCart customerCart) throws PasswordOrUserNameWrong;
    CustomerCart updateCustomerCart(CustomerCart customerCart);
    CustomerCart getCustomerCart(Integer id);
    CustomerCart findCustomerCartByCustomerId(Integer customerId);

}
