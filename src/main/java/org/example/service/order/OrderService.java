package org.example.service.order;

import org.example.domain.Employee;
import org.example.domain.Order;

import java.util.List;

public interface OrderService {
    void save(Order order);
    void update(Order order);
    void delete(int id);
    List<Order> findAll();
    List<Order> findOrdersForEmployee(Employee employee);
    Order findById(int id);
}
