package org.example.repository.order.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.example.domain.Employee;
import org.example.domain.Orders;
import org.example.repository.order.OrderRepository;
import org.example.util.HibernateUtil;

import java.util.List;

public class OrderRepositoryImp implements OrderRepository {
    @Override
    public void save(Orders orders) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(orders);
            entityManager.getTransaction().commit();
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void update(Orders orders) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(orders);
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
            Orders orders = entityManager.find(Orders.class, orderId);
            entityManager.getTransaction().begin();
            entityManager.remove(orders);
            entityManager.getTransaction().commit();
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public Orders findById(int orderId) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        try {
            Orders orders = entityManager.find(Orders.class, orderId);
            return orders;
        }catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Orders> findAll() {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
        Query query = entityManager.createQuery("from Orders order", Orders.class);
        List<Orders> orders = query.getResultList();
        return orders;
    }

    @Override
    public List<Orders> selectByEmployeeSubHandler(Employee employee) {
        EntityManager entityManager = HibernateUtil.getInstance().getEntityManager();
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
