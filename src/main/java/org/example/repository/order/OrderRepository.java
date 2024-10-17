package org.example.repository.order;

import org.example.domain.Employee;
import org.example.domain.Orders;

import java.util.List;

public interface OrderRepository {
     void save(Orders orders);
    void update(Orders orders);
    void delete(int orderId);
    Orders findById(int orderId);
    List<Orders> findAll();
    List<Orders> selectByEmployeeSubHandler(Integer employeeId);
    List<Orders> selectOrdersByCustomer(Integer customerId);
}
