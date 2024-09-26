package org.example.repository.user.admin.imp;

import jakarta.persistence.EntityManager;
import org.example.domain.Employee;
import org.example.domain.Handler;
import org.example.domain.SubHandler;
import org.example.repository.user.admin.AdminRepository;
import org.example.util.HibernateUtil;

public class AdminRepositoryImp implements AdminRepository {
    @Override
    public void saveHandler(Handler handler) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(handler);
            entityManager.getTransaction().commit();
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void saveSubHandler(SubHandler subHandler, Integer handlerId) {

    }

    @Override
    public void saveEmployeeToSubHandler(Employee employee, Integer subHandlerId) {

    }

    @Override
    public void deleteEmployeeFromSubHandler(Employee employee, Integer subHandlerId) {

    }
}
