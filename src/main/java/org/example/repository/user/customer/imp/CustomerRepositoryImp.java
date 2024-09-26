package org.example.repository.user.customer.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.example.domain.Customer;
import org.example.repository.user.BaseUserRepositoryImp;
import org.example.repository.user.customer.CustomerRepository;
import org.example.util.HibernateUtil;

public class CustomerRepositoryImp extends BaseUserRepositoryImp<Customer> implements CustomerRepository {
    @Override
    public Customer login(String username, String password) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            Query query = entityManager.createNativeQuery("""
                   select * from passanduser
           where typeofuser = 'customer' and username = ?
           and pass = ?
""");
            query.setParameter(1, username);
            query.setParameter(2, password);
            return (Customer) query.getSingleResult();
        }catch (Exception e) {
            return null;
        }
        }
}
