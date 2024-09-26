package org.example.repository.user.employee;

import org.example.domain.Employee;
import org.example.repository.user.BaseUserRepository;

public interface EmployeeRepository extends BaseUserRepository<Employee> {
    Employee login (String username, String password);
}
