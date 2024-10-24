package org.example.service.order.imp;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.domain.Employee;
import org.example.domain.Orders;
import org.example.exeptions.OrderStateIsNotCorrect;
import org.example.exeptions.TimeOfWorkDoesntMatch;
import org.example.repository.order.OrderRepository;
import org.example.service.order.OrderService;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService {
    private final OrderRepository orderRepository ;
    @Override
    @Transactional
    public void save(Orders orders) throws TimeOfWorkDoesntMatch {
        if (orders.getTimeOfWork().isBefore(LocalDateTime.now()))
        {
            throw new TimeOfWorkDoesntMatch();
        }
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
    public List<Orders> findGotOrdersForEmployee(Integer employeeId) {
        return orderRepository.selectGotOrdersByEmployeeId(employeeId);
    }

    @Override
    public Orders findById(int id) {
        return orderRepository.findById(id);
    }
    @Override
    public List<Orders> findAllOrdersThatHaveSameCustomer(Integer customerId) throws OrderStateIsNotCorrect {
        List<Orders> allDefiend =orderRepository.selectOrdersByCustomer(customerId);
        if (allDefiend.isEmpty()) {
            throw new OrderStateIsNotCorrect();
        }
        return allDefiend;
    }
    @Override
    public List<Orders> findOrdersForSubHandler(Integer subHandlerId) {
        return orderRepository.selectOrdersBySubHandlerId(subHandlerId);
    }

    @Override
    public List<Orders> findPaidOrdersForEmployee(Integer employeeId) {
        return orderRepository.selectPaidOrdersForEmployee(employeeId);
    }

    @Override
    public List<Orders> findPaidOrdersForCustomer(Integer employeeId) {
        return orderRepository.selectPaidOrdersForCustomer(employeeId);

    }

    @Override
    public List<Orders> optionalFindOrders(LocalDate startDate, LocalDate endDate, List<String> handlersName, List<String> subHandlers) {
        return orderRepository.optionalSelectOrders(startDate, endDate, handlersName, subHandlers);
    }
}
