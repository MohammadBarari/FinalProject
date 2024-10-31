package org.example.repository.user.customer;

import org.example.domain.Customer;
import org.example.domain.Employee;
import org.example.dto.admin.CustomerOutputDtoForReport;
import org.example.repository.user.BaseUserRepository;

import java.time.LocalDate;
import java.util.List;

public interface CustomerRepository extends BaseUserRepository<Customer> {
    Customer login (String username, String password);
    List<Customer> selectCustomerByOptional(String name, String lastName, String email, String phone);
    Customer findByEmail(String email);
    List<CustomerOutputDtoForReport> selectCustomerByReports(LocalDate startDate, LocalDate endDate,Integer doneOrderStart,Integer doneOrderEnd);
    Customer findByUser(String user);
}
