package org.example.repository.handler.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.example.domain.Handler;
import org.example.repository.handler.HandlerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class HandlerRepositoryImp implements HandlerRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    @Transactional
    public void save(Handler handler) {

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(handler);

        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    @Transactional
    public void update(Handler handler) {

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(handler);

        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    @Transactional
    public void delete(int id) {

        try {
            Handler handler = entityManager.find(Handler.class, id);
            entityManager.getTransaction().begin();
            entityManager.remove(handler);

        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public Handler findById(int id) {

        return entityManager.find(Handler.class, id);
    }

    @Override
    public List<Handler> findAll() {

        try {
            Query query = entityManager.createQuery("from Handler",Handler.class);
            return query.getResultList();
        }catch (Exception e) {
            return null;
        }
    }
}
