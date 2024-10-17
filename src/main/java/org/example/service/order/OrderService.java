package org.example.service.order;

import org.example.domain.Employee;
import org.example.domain.Orders;
import org.example.exeptions.OrderStateIsNotCorrect;
import org.hibernate.query.Order;

import java.util.List;

public interface OrderService {
    void save(Orders orders);
    void update(Orders orders);
    void delete(int id);
    List<Orders> findAll();
    List<Orders> findOrdersForEmployee(Integer employeeId);
    Orders findById(int id);
    List<Orders> findAllOrdersThatHaveSameCustomer(Integer customerId) throws OrderStateIsNotCorrect;
    List<Orders> findOrdersForSubHandler(Integer subHandlerId);
}
