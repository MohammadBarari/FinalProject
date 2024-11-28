package org.example.service.order.imp;

import lombok.RequiredArgsConstructor;
import org.example.domain.Orders;
import org.example.exeptions.NotFoundException.NotFoundOrder;
import org.example.exeptions.order.OrderStateIsNotCorrect;
import org.example.exeptions.wrongTime.TimeOfWorkDoesntMatch;
import org.example.repository.order.CustomOrderRepository;
import org.example.repository.order.OrderRepository;
import org.example.service.order.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService {
    private final OrderRepository orderRepository ;
    private final CustomOrderRepository orderCustomRepository ;
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
        orderRepository.save(orders);
    }

    @Override
    @Transactional
    public void delete(int id) {
        orderRepository.deleteById(id);
    }
    @Override
    public List<Orders> findAll() {
        return List.of();
    }
    @Override
    public List<Orders> findOrdersForEmployee(Integer employeeId) {
        return orderRepository.findOrdersByEmployee(employeeId);
    }

    @Override
    public List<Orders> findGotOrdersForEmployee(Integer employeeId) {
        return orderRepository.selectGotOrdersByEmployeeId(employeeId);
    }

    @Override
    public Orders findById(int id) {
        return orderRepository.findOrdersById(id);
    }
    @Override
    public List<Orders> findAllOrdersThatHaveSameCustomer(Integer customerId) throws OrderStateIsNotCorrect {
        List<Orders> allDefined =orderRepository.findOrdersByCustomer(customerId);
        if (allDefined.isEmpty()) {
            throw new NotFoundOrder("Not found any order with customer id :" + customerId);
        }
        return allDefined;
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
        return orderCustomRepository.optionalSelectOrders(startDate, endDate, handlersName, subHandlers);
    }

    @Override
    public List<Orders> optionalFindOrdersForEmployee(Integer employeeId, String orderState) {
        return orderCustomRepository.optionalSelectOrdersForEmployee(employeeId, orderState);
    }

    @Override
    public List<Orders> optionalFindOrdersForCustomer(Integer employeeId, String orderState) {
        return orderCustomRepository.optionalSelectOrdersForCustomer(employeeId, orderState);
    }
}
