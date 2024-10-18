package org.example.repository.user.employee;

import org.example.domain.Employee;
import org.example.repository.user.BaseUserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface EmployeeRepository extends BaseUserRepository<Employee> {
    Employee login (String username, String password);
    Boolean employeeExistsById(Integer id);
    List<Employee> selectEmployeesByOptionalInformation(String name, String lastName, String email, String phone, String handlerName);
}
