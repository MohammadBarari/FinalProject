package org.example.repository.user.employee.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.domain.Employee;
import org.example.repository.user.BaseUserRepositoryImp;
import org.example.repository.user.employee.EmployeeRepository;

public class  EmployeeRepositoryImp extends BaseUserRepositoryImp<Employee> implements EmployeeRepository {

   @PersistenceContext
   private EntityManager entityManager;
    @Override
    public Employee login(String username, String password) {
        try {
            Query query = entityManager.createNativeQuery("""
           select * from passanduser
           where typeofuser = 'employee' and username = ?
           and pass = ?
   """);
            query.setParameter(1, username);
            query.setParameter(2, password);
            Employee employee = (Employee) query.getSingleResult();
            return employee;
        }catch (Exception e) {
            return null;
        }
    }
}
