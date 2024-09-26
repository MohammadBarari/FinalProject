package org.example.repository.subHandler.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.example.domain.Handler;
import org.example.domain.SubHandler;
import org.example.repository.subHandler.SubHandlerRepository;
import org.example.util.HibernateUtil;

import java.util.List;

public class SubHandlerRepositoryImp implements SubHandlerRepository {
    @Override
    public void save(SubHandler subHandler) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(subHandler);
            entityManager.getTransaction().commit();
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void delete(Integer id) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            entityManager.getTransaction().begin();
            SubHandler subHandler = entityManager.find(SubHandler.class, id);
            entityManager.remove(subHandler);
            entityManager.getTransaction().commit();
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
        }

    @Override
    public void update(SubHandler subHandler) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(subHandler);
            entityManager.getTransaction().commit();
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public SubHandler selectById(Integer id) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
             return entityManager.find(SubHandler.class, id);
        }catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<SubHandler> selectBySameHandler(Handler handler) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            Query query = entityManager.createNativeQuery("""
        select * from testforfinalproject.subhandler
        where handler_id= ?
""", SubHandler.class);

            query.setParameter(1, handler.getId());
            return query.getResultList();
        }catch (Exception e) {
            return null;
        }
        }
}
