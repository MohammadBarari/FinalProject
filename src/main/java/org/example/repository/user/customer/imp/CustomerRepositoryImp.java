package org.example.repository.user.customer.imp;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.example.domain.Customer;
import org.example.domain.Orders;
import org.example.dto.admin.CustomerOutputDtoForReport;
import org.example.repository.user.BaseUserRepositoryImp;
import org.example.repository.user.customer.CustomerRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Override
    public List<CustomerOutputDtoForReport> selectCustomerByReports(LocalDate startDate, LocalDate endDate, Integer doneOrderStart, Integer doneOrderEnd) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CustomerOutputDtoForReport> query = cb.createQuery(CustomerOutputDtoForReport.class);
        Root<Customer> customer = query.from(Customer.class);
        List<Predicate> predicates = new ArrayList<>();

        if (startDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            predicates.add(cb.greaterThanOrEqualTo(customer.get("timeOfRegistration"), startDateTime));
        }

        if (endDate != null) {
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
            predicates.add(cb.lessThanOrEqualTo(customer.get("timeOfRegistration"), endDateTime));
        }

        Expression<Long> worksPaid = null;
        List<Predicate> havingOrdersPaidPredicates = new ArrayList<>();
        Root<Orders> ordersRoot = query.from(Orders.class);
        Join<Orders, Customer> ordersJoin = ordersRoot.join("customer", JoinType.LEFT);
        worksPaid = getWorksPaid(cb, ordersRoot,ordersJoin,customer);
        if (doneOrderStart != null || doneOrderEnd != null) {
            predicates.add(cb.equal(ordersJoin.get("id"), customer.get("id")));
            if (doneOrderStart != null) {
                havingOrdersPaidPredicates.add(cb.greaterThanOrEqualTo(worksPaid, doneOrderStart.longValue()));
            }
            if (doneOrderEnd != null) {
                havingOrdersPaidPredicates.add(cb.lessThanOrEqualTo(worksPaid, doneOrderEnd.longValue()));
            }
        }
        query.groupBy(
                customer.get("id"),
                customer.get("name"),
                customer.get("last_name"),
                customer.get("email"),
                customer.get("phone"),
                customer.get("timeOfRegistration"),
                customer.get("isActive")
        );
        query.select(cb.construct(CustomerOutputDtoForReport.class,
                customer.get("id"),
                customer.get("name"),
                customer.get("last_name"),
                customer.get("email"),
                customer.get("phone"),
                customer.get("timeOfRegistration"),
                customer.get("isActive"),
                worksPaid == null ? cb.literal(null) : worksPaid
        )
        ).where(cb.and(predicates.toArray(new Predicate[0])));
        if (!havingOrdersPaidPredicates.isEmpty()) {
            query.having(havingOrdersPaidPredicates.toArray(new Predicate[0]));
        }
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Customer findByUser(String user) {
        try {
            Query query = entityManager.createNativeQuery("""
        select customer.* from customer where phone = ?
""", Customer.class);
            query.setParameter(1, user);
            return (Customer) query.getSingleResult();
        }catch (Exception e) {
            return null;
        }
    }


    private Expression<Long> getWorksPaid(CriteriaBuilder cb, Root<Orders> orderRoot,Join<Orders, Customer> ordersJoinJoin,Root<Customer> customerRoot) {
        Join<Orders, Customer> a = orderRoot.join("customer", JoinType.INNER);
        Predicate isPaid = cb.equal(orderRoot.get("orderState"), "PAID");
        Predicate customerExists =cb.equal(orderRoot.get("customer").get("id"), customerRoot.get("id"));
        Predicate predicate2 = cb.and(isPaid,customerExists);
        Expression<Long> caseExpression = cb.selectCase()
                .when(predicate2, 1L)
                .otherwise(0L).as(Long.class);
        return cb.sum(caseExpression).as(Long.class);
    }

}
