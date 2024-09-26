package org.example.repository.credit.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.example.domain.Credit;
import org.example.repository.credit.CreditRepository;
import org.example.util.HibernateUtil;

import java.util.List;

public class CreditRepositoryImp implements CreditRepository {
    @Override
    public void save(Credit credit) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(credit);
            entityManager.getTransaction().commit();
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void update(Credit credit) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(credit);
            entityManager.getTransaction().commit();
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void delete(int creditId) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            Credit credit = entityManager.find(Credit.class, creditId);
            entityManager.getTransaction().begin();
            entityManager.remove(credit);
            entityManager.getTransaction().commit();
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public Credit selectCreditById(int creditId) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            Credit credit = entityManager.find(Credit.class, creditId);
            return credit;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<Credit> selectAllCredits() {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            Query query = entityManager.createQuery("from Credit",Credit.class);
            return query.getResultList();
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public Credit selectByUserId(int userId) {
        En
    }
}
