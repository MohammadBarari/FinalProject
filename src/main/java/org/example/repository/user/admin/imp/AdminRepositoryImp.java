package org.example.repository.user.admin.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.example.domain.Employee;
import org.example.domain.Handler;
import org.example.repository.user.admin.AdminRepository;

public class AdminRepositoryImp implements AdminRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    @Transactional
    public void saveHandler(Handler handler) {
        try {
            entityManager.persist(handler);
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }


    @Override
    @Transactional
    public void deleteEmployeeFromSubHandler(Employee employee, Integer subHandlerId) {
        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createNativeQuery("""
    delete from finalproject.employee_subhandler where employee_id = ?
    and subhandlers_id = ?
""");
            query.setParameter(1, employee.getId());
            query.setParameter(2, subHandlerId);
            query.executeUpdate();
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
            throw e;
        }
    }
}
