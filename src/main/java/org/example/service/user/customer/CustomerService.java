package org.example.service.user.customer;

import org.example.domain.Customer;
import org.example.dto.CustomerSignUpDto;
import org.example.service.user.BaseUserService;

public interface CustomerService extends BaseUserService<Customer> {
    CustomerSignUpDto signUpCustomer(CustomerSignUpDto customerDto);
    boolean validateCustomer(CustomerSignUpDto customerDto);
}
