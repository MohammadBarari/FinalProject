package org.example.service.order;

import org.example.domain.Employee;
import org.example.domain.Orders;

import java.util.List;

public interface OrderService {
    void save(Orders orders);
    void update(Orders orders);
    void delete(int id);
    List<Orders> findAll();
    List<Orders> findOrdersForEmployee(Employee employee);
    Orders findById(int id);
}
