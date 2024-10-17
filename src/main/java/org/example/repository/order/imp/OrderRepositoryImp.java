package org.example.repository.order.imp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.example.domain.Customer;
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
    public void save(Orders orders) {
            entityManager.persist(orders);
    }

    @Override
    public void update(Orders orders) {
        entityManager.merge(orders);
    }

    @Override
    public void delete(int orderId) {
            Orders orders = entityManager.find(Orders.class, orderId);
            entityManager.remove(orders);
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
        public List<Orders> selectByEmployeeSubHandler(Integer employeeId) {
        try {
            Query query = entityManager.createNativeQuery("""
        select * from testforfinalproject.orders
        where employee_id = ?
""", Orders.class);
            query.setParameter(1, employeeId);
            List<Orders> orders = query.getResultList();
            return orders;
        }catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Orders> selectOrdersByCustomer(Integer customerId) {
        try {
            Query query = entityManager.createNativeQuery("""
        select * from testforfinalproject.orders where customer_id = ?
""", Customer.class);
        return  query.setParameter(1, customerId).getResultList();
        }catch (Exception e){
            return null;
        }
    }
}
