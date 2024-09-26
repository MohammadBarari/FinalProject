package org.example.repository.handler.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.example.domain.Handler;
import org.example.repository.handler.HandlerRepository;
import org.example.util.HibernateUtil;

import java.util.List;

public class HandlerRepositoryImp implements HandlerRepository {
    @Override
    public void save(Handler handler) {
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
    public void update(Handler handler) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(handler);
            entityManager.getTransaction().commit();
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void delete(int id) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            Handler handler = entityManager.find(Handler.class, id);
            entityManager.getTransaction().begin();
            entityManager.remove(handler);
            entityManager.getTransaction().commit();
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public Handler findById(int id) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        return entityManager.find(Handler.class, id);
    }

    @Override
    public List<Handler> findAll() {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            Query query = entityManager.createQuery("from Handler",Handler.class);
            return query.getResultList();
        }catch (Exception e) {
            return null;
        }
    }
}
