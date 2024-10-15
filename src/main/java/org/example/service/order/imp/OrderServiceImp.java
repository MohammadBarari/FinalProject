package org.example.service.order.imp;

import lombok.RequiredArgsConstructor;
import org.example.domain.Employee;
import org.example.domain.Orders;
import org.example.repository.order.OrderRepository;
import org.example.service.order.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService {
    private final OrderRepository orderRepository ;
    @Override
    @Transactional
    public void save(Orders orders) {
        orderRepository.save(orders);
    }

    @Override
    @Transactional
    public void update(Orders orders) {
        orderRepository.update(orders);

    }

    @Override
    @Transactional
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
