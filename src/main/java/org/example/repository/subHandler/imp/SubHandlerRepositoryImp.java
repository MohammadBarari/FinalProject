package org.example.repository.subHandler.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.example.domain.Handler;
import org.example.domain.SubHandler;
import org.example.repository.subHandler.SubHandlerRepository;
import org.example.util.HibernateUtil;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class SubHandlerRepositoryImp implements SubHandlerRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    @Transactional
    public void save(SubHandler subHandler) {

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(subHandler);

        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            SubHandler subHandler = entityManager.find(SubHandler.class, id);
            entityManager.remove(subHandler);
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
        }

    @Override
    @Transactional
    public void update(SubHandler subHandler) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            entityManager.merge(subHandler);
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public SubHandler selectById(Integer id) {
        try {
             return entityManager.find(SubHandler.class, id);
        }catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<SubHandler> selectBySameHandler(Handler handler) {
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
