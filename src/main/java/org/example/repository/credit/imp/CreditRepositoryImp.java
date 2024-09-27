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
    public Credit selectByCustomerId(int customerId) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            Query query = entityManager.createNativeQuery("""
            select c.id from customer join credit c on c.id = customer.credit_id
        where customer.id = ?;
            """,Integer.class);
            query.setParameter(1, customerId);
            List<Object> credit =  query.getResultList();
            Integer integer = (Integer) credit.get(0);
            Query query1 = entityManager.createNativeQuery("""
            select * from credit
            where id = ?
""",Credit.class);
            query1.setParameter(1, integer);

            Credit credit1 = (Credit) query1.getSingleResult();
            return credit1;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Credit selectByEmployeeId(int employeeId) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            Query query = entityManager.createNativeQuery("""
            select c.id from employee join credit c on c.id = employee.credit_id
        where employee.id = 1;
            """,Integer.class);
            List<Object> credit =  query.getResultList();
            Integer integer = (Integer) credit.get(0);
            Query query1 = entityManager.createNativeQuery("""
            select * from testforfinalproject.credit
            where id = ?
""",Credit.class);
            query1.setParameter(1, integer);

            Credit credit1 = (Credit) query1.getSingleResult();
            return credit1;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void payToEmployee(Integer customerCreditId, Integer employeeCreditId, Long offerPrice) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            entityManager.getTransaction().begin();
            Credit creditCustomer = entityManager.find(Credit.class, customerCreditId);
            Credit creditEmployee = entityManager.find(Credit.class, employeeCreditId);
            creditCustomer.setAmount(creditCustomer.getAmount() - offerPrice);
            creditEmployee.setAmount(creditEmployee.getAmount() + offerPrice);
            entityManager.getTransaction().commit();
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

}
