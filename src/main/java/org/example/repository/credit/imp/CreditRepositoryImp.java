package org.example.repository.credit.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.domain.Credit;
import org.example.repository.credit.CreditRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class CreditRepositoryImp implements CreditRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public void save(Credit credit) {
            entityManager.persist(credit);
    }

    @Override
    public void update(Credit credit) {
            entityManager.merge(credit);
    }

    @Override
    public void delete(int creditId) {
            Credit credit = entityManager.find(Credit.class, creditId);
            entityManager.remove(credit);
    }

    @Override
    public Credit selectCreditById(int creditId) {
        try {
            return entityManager.find(Credit.class, creditId);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<Credit> selectAllCredits() {
        try {
            Query query = entityManager.createQuery("from Credit",Credit.class);
            return query.getResultList();
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public Credit selectByCustomerId(int customerId) {
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

        try {
            Query query = entityManager.createNativeQuery("""
            select c.id from employee join credit c on c.id = employee.credit_id
        where employee.id = ?;
            """,Integer.class);
            query.setParameter(1, employeeId);
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
    public void payToEmployee(Integer customerCreditId, Integer employeeCreditId, Long offerPrice) {

        try {
            entityManager.getTransaction().begin();
            Credit creditCustomer = entityManager.find(Credit.class, customerCreditId);
            Credit creditEmployee = entityManager.find(Credit.class, employeeCreditId);
            creditCustomer.setAmount(creditCustomer.getAmount() - offerPrice);
            creditEmployee.setAmount(creditEmployee.getAmount() + offerPrice);

        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

}
