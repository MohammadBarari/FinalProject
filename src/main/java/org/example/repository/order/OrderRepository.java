package org.example.repository.order;

import org.example.domain.Employee;
import org.example.domain.Order;

import java.util.List;

public interface OrderRepository {
     void save(Order order);
    void update(Order order);
    void delete(int orderId);
    Order findById(int orderId);
    List<Order> findAll();
    List<Order> selectByEmployeeSubHandler(Employee employee);
}
