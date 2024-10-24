package org.example.service.order;

import org.example.domain.Employee;
import org.example.domain.Orders;
import org.example.enumirations.OrderState;
import org.example.exeptions.OrderStateIsNotCorrect;
import org.example.exeptions.TimeOfWorkDoesntMatch;
import org.hibernate.query.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    void save(Orders orders) throws TimeOfWorkDoesntMatch;
    void update(Orders orders);
    void delete(int id);
    List<Orders> findAll();
    List<Orders> findOrdersForEmployee(Integer employeeId);
    List<Orders> findGotOrdersForEmployee(Integer employeeId);
    Orders findById(int id);
    List<Orders> findAllOrdersThatHaveSameCustomer(Integer customerId) throws OrderStateIsNotCorrect;
    List<Orders> findOrdersForSubHandler(Integer subHandlerId);
    List<Orders> findPaidOrdersForEmployee(Integer employeeId);
    List<Orders> findPaidOrdersForCustomer(Integer employeeId);
    List<Orders> optionalFindOrders(LocalDate startDate, LocalDate endDate, List<String> handlersName, List<String> subHandlers);
    List<Orders> optionalFindOrdersForEmployee(Integer employeeId, String orderState);
    List<Orders> optionalFindOrdersForCustomer(Integer employeeId, String orderState);

}
