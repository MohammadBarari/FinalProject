package org.example.repository.user.employee.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.example.domain.Employee;
import org.example.repository.user.BaseUserRepositoryImp;
import org.example.repository.user.employee.EmployeeRepository;
import org.example.util.HibernateUtil;

public class EmployeeRepositoryImp extends BaseUserRepositoryImp<Employee> implements EmployeeRepository {
    @Override
    public Employee login(String username, String password) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
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
