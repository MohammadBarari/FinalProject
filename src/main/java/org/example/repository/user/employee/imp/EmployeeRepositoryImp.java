package org.example.repository.user.employee.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.example.domain.Employee;
import org.example.domain.Handler;
import org.example.domain.SubHandler;
import org.example.repository.user.BaseUserRepositoryImp;
import org.example.repository.user.employee.EmployeeRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Primary
public class  EmployeeRepositoryImp extends BaseUserRepositoryImp<Employee> implements EmployeeRepository {



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

    @Override
    public Boolean employeeExistsById(Integer id) {
        try {
            Query query = entityManager.createNativeQuery("""
             SELECT COUNT(e) FROM Employee e WHERE e.id = ?
""",Integer.class);
            query.setParameter(1, id);
            Integer count = (Integer) query.getSingleResult();
            return count > 0;
        }catch (Exception e) {
            return false;
        }
    }

    public List<Employee> selectEmployeesByOptionalInformation(String name, String lastName, String email, String phone, String handlerName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> employee = query.from(Employee.class);
        List<Predicate> predicates = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            predicates.add(cb.like(employee.get("name"), "%" + name + "%"));
        }
        if (lastName != null && !lastName.isEmpty()) {
            predicates.add(cb.like(employee.get("last_name"), "%" + lastName + "%"));
        }
        if (email != null && !email.isEmpty()) {
            predicates.add(cb.like(employee.get("email"), "%" + email + "%"));
        }
        if (phone != null && !phone.isEmpty()) {
            predicates.add(cb.like(employee.get("phone"), "%" + phone + "%"));
        }
//        if (handlerName != null && !handlerName.isEmpty()) {
//            predicates.add(cb.like(employee.join("subHandlers").join("handler").get("name"), "%" + handlerName + "%"));
//        }
        if (handlerName != null && !handlerName.isEmpty()) {
            if (handlerName != null && !handlerName.isEmpty()) {
                Join<Employee, SubHandler> subHandlerJoin = employee.join("subHandlers");
                Join<SubHandler, Handler> handlerJoin = subHandlerJoin.join("handler");

                predicates.add(cb.like(handlerJoin.get("name"), "%" + handlerName + "%"));
            }
        }

        query.select(employee).where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }
}
