package org.example.repository.user.customer;

import org.example.domain.Customer;
import org.example.repository.user.BaseUserRepository;

public interface CustomerRepository extends BaseUserRepository<Customer> {
    Customer login (String username, String password);
}
