package org.example.repository.user.admin.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.example.domain.Employee;
import org.example.domain.Handler;
import org.example.repository.user.admin.AdminRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AdminRepositoryImp implements AdminRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public void saveHandler(Handler handler) {
            entityManager.persist(handler);
    }


    @Override
    public void deleteEmployeeFromSubHandler(Employee employee, Integer subHandlerId) {

            Query query = entityManager.createNativeQuery("""
    delete from employee_sub_handlers where employee_id = ?
    and sub_handlers_id = ?
""");
            query.setParameter(1, employee.getId());
            query.setParameter(2, subHandlerId);
            query.executeUpdate();
    }
}
