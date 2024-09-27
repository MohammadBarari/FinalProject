package org.example.service.order.imp;

import org.example.domain.Employee;
import org.example.domain.Orders;
import org.example.repository.order.OrderRepository;
import org.example.repository.order.imp.OrderRepositoryImp;
import org.example.service.order.OrderService;

import java.util.List;

public class OrderServiceImp implements OrderService {
    OrderRepository orderRepository = new OrderRepositoryImp();
    @Override
    public void save(Orders orders) {
        orderRepository.save(orders);
    }

    @Override
    public void update(Orders orders) {
        orderRepository.update(orders);

    }

    @Override
    public void delete(int id) {
        orderRepository.delete(id);
    }

    @Override
    public List<Orders> findAll() {
        return List.of();
    }

    @Override
    public List<Orders> findOrdersForEmployee(Employee employee) {
        return orderRepository.selectByEmployeeSubHandler(employee);
    }

    @Override
    public Orders findById(int id) {
        return orderRepository.findById(id);
    }

}
