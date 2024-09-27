package org.example.service.user.customer;

import org.example.domain.Customer;
import org.example.domain.Users;
import org.example.dto.CustomerSignUpDto;
import org.example.service.user.BaseUserService;

public interface CustomerService extends BaseUserService<Customer> {
    void signUpCustomer(CustomerSignUpDto customerDto);
    boolean validateCustomer(CustomerSignUpDto customerDto);
}
