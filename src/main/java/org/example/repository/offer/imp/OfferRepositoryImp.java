package org.example.repository.offer.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.example.domain.Offer;
import org.example.repository.offer.OfferRepository;
import org.example.util.HibernateUtil;

import java.util.List;

public class OfferRepositoryImp implements OfferRepository {
    @Override
    public void save(Offer offer) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(offer);
            entityManager.getTransaction().commit();
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void update(Offer offer) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(offer);
            entityManager.getTransaction().commit()
        }catch (Exception e){
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void delete(int id) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
           Offer offer = entityManager.find(Offer.class, id);
           entityManager.getTransaction().begin();
           entityManager.remove(offer);
           entityManager.getTransaction().commit();
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public Offer findById(int id) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            return entityManager.find(Offer.class, id);
        }catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Offer> findAllForOrder(int orderId) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            Query query = entityManager.createNativeQuery("""
        select * from offer
""", Offer.class);
            return query.getResultList();
        }catch (Exception e) {
            return null;
        }
        }
}