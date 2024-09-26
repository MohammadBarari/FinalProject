package org.example.repository.order.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.example.domain.Employee;
import org.example.domain.Order;
import org.example.repository.order.OrderRepository;
import org.example.util.HibernateUtil;

import java.util.List;

public class OrderRepositoryImp implements OrderRepository {
    @Override
    public void save(Order order) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(order);
            entityManager.getTransaction().commit();
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void update(Order order) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(order);
            entityManager.getTransaction().commit();
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void delete(int orderId) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            Order order = entityManager.find(Order.class, orderId);
            entityManager.getTransaction().begin();
            entityManager.remove(order);
            entityManager.getTransaction().commit();
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public Order findById(int orderId) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            Order order = entityManager.find(Order.class, orderId);
            return order;
        }catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Order> findAll() {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        Query query = entityManager.createQuery("from Order order",Order.class);
        List<Order> orders = query.getResultList();
        return orders;
    }

    @Override
    public List<Order> selectByEmployeeSubHandler(Employee employee) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            Query query = entityManager.createNativeQuery("""
        select * from testforfinalproject.orders
        where employee_id = ?
""",Order.class);
            query.setParameter(1, employee.getId());
            List<Order> orders = query.getResultList();
            return orders;
        }catch (Exception e) {
            return null;
        }
    }
}
