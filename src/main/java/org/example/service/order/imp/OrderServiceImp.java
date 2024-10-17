package org.example.service.order.imp;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.domain.Employee;
import org.example.domain.Orders;
import org.example.exeptions.NotFoundSomething;
import org.example.exeptions.OrderStateIsNotCorrect;
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
    public List<Orders> findOrdersForEmployee(Integer employeeId) {
        return orderRepository.selectByEmployeeSubHandler(employeeId);
    }

    @Override
    public Orders findById(int id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Orders> findAllOrdersThatHaveSameCustomer(Integer customerId) throws OrderStateIsNotCorrect {
        List<Orders> allDefind =orderRepository.selectOrdersByCustomer(customerId);
        if (allDefind.isEmpty()) {
            throw new OrderStateIsNotCorrect();
        }
        return allDefind;
    }

    @Override
    public List<Orders> findOrdersForSubHandler(Integer subHandlerId) {
        return orderRepository.selectOrdersBySubHandlerId(subHandlerId);
    }

}
