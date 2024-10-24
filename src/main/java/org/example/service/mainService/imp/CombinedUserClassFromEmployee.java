package org.example.service.mainService.imp;
import org.example.domain.Customer;
import org.example.service.user.customer.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class CombinedUserClassFromEmployee {
    private final CustomerService customerService;
    public CombinedUserClassFromEmployee(CustomerService customerService) {
        this.customerService = customerService;
    }
    public Customer getCustomer(Integer id) {
        return customerService.findById(id,Customer.class);
    }
}
