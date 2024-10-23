package org.example.repository.user.customer.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.domain.Customer;
import org.example.repository.user.BaseUserRepositoryImp;
import org.example.repository.user.customer.CustomerRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerRepositoryImp extends BaseUserRepositoryImp<Customer> implements CustomerRepository {

    @Override
    public Customer login(String username, String password) {
        try {
            Query query = entityManager.createNativeQuery("""
select customer.* from customer join pass_and_user pau on pau.id = customer.pass_and_user_id
where  pau.username= ? and pau.pass = ?
""",Customer.class);
            query.setParameter(1, username);
            query.setParameter(2, password);
            return (Customer) query.getSingleResult();
        }catch (Exception e) {
            return null;
        }
        }
    public List<Customer> selectCustomerByOptional(String name, String lastName, String email, String phone) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> query = cb.createQuery(Customer.class);
        Root<Customer> customer = query.from(Customer.class);
        List<Predicate> predicates = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            predicates.add(cb.like(customer.get("name"), "%" + name + "%"));
        }
        if (lastName != null && !lastName.isEmpty()) {
            predicates.add(cb.like(customer.get("last_name"), "%" + lastName + "%"));
        }
        if (email != null && !email.isEmpty()) {
            predicates.add(cb.like(customer.get("email"), "%" + email + "%"));
        }
        if (phone != null && !phone.isEmpty()) {
            predicates.add(cb.like(customer.get("phone"), "%" + phone + "%"));
        }
        query.select(customer).where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Customer findByEmail(String email) {
        try {
            Query query = entityManager.createNativeQuery("""
    select * from customer where email = ?
""",Customer.class);
            query.setParameter(1, email);
            return (Customer) query.getSingleResult();
            }catch (Exception e) {
            return null;
        }
    }
}
