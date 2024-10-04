package org.example.repository.order.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.example.domain.Employee;
import org.example.domain.Orders;
import org.example.repository.order.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class OrderRepositoryImp implements OrderRepository {
    @PersistenceContext
    EntityManager entityManager;
    @Override
    @Transactional
    public void save(Orders orders) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(orders);
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    @Transactional
    public void update(Orders orders) {

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(orders);

        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    @Transactional
    public void delete(int orderId) {

        try {
            Orders orders = entityManager.find(Orders.class, orderId);
            entityManager.getTransaction().begin();
            entityManager.remove(orders);

        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public Orders findById(int orderId) {
        try {
            Orders orders = entityManager.find(Orders.class, orderId);
            return orders;
        }catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Orders> findAll() {
        Query query = entityManager.createQuery("from Orders order", Orders.class);
        List<Orders> orders = query.getResultList();
        return orders;
    }

    @Override
    public List<Orders> selectByEmployeeSubHandler(Employee employee) {
        try {
            Query query = entityManager.createNativeQuery("""
        select * from testforfinalproject.orders
        where employee_id = ?
""", Orders.class);
            query.setParameter(1, employee.getId());
            List<Orders> orders = query.getResultList();
            return orders;
        }catch (Exception e) {
            return null;
        }
    }
}
