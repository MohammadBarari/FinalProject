package org.example.repository.offer.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.example.domain.Offer;
import org.example.repository.offer.OfferRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class OfferRepositoryImp implements OfferRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(Offer offer) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(offer);
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    @Transactional
    public void update(Offer offer) {

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(offer);

        }catch (Exception e){
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    @Transactional
    public void delete(int id) {

        try {
           Offer offer = entityManager.find(Offer.class, id);
           entityManager.getTransaction().begin();
           entityManager.remove(offer);

        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public Offer findById(int id) {

        try {
            return entityManager.find(Offer.class, id);
        }catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Offer> findAllForOrder(int orderId) {

        try {
            Query query = entityManager.createNativeQuery("""
        select * from offer
""", Offer.class);
            return query.getResultList();
        }catch (Exception e) {
            return null;
        }
    }

    @Override
    public Offer selectAcceptedOfferInOrder(Integer id) {

        try {
            Query query = entityManager.createNativeQuery("""
        select offer.id from offer
        join orders o on o.id = offer.order_id
        where o.id =? and offer.accepted = true;
""",Integer.class);
            query.setParameter(1, id);
            int offerId = (Integer) query.getSingleResult();
            Query query1 = entityManager.createNativeQuery("""
        select * from offer where id = ?
""",Offer.class);
            query1.setParameter(1, offerId);
       return (Offer) query1.getSingleResult();
        }catch (Exception e) {
            return null;
        }
    }

}
