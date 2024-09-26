package org.example.service.order.imp;

import org.example.domain.Employee;
import org.example.domain.Order;
import org.example.repository.order.OrderRepository;
import org.example.repository.order.imp.OrderRepositoryImp;
import org.example.service.order.OrderService;

import java.util.List;

public class OrderServiceImp implements OrderService {
    OrderRepository orderRepository = new OrderRepositoryImp();
    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    public void update(Order order) {
        orderRepository.update(order);

    }

    @Override
    public void delete(int id) {
        orderRepository.delete(id);
    }

    @Override
    public List<Order> findAll() {
        return List.of();
    }

    @Override
    public List<Order> findOrdersForEmployee(Employee employee) {
        return orderRepository.selectByEmployeeSubHandler(employee);
    }

    @Override
    public Order findById(int id) {
        return orderRepository.findById(id);
    }

}
