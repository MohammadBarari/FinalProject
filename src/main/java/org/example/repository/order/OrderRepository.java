package org.example.repository.order;

import org.example.domain.Orders;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository {
     void save(Orders orders);
    void update(Orders orders);
    void delete(int orderId);
    Orders findById(int orderId);
    List<Orders> findAll();
    List<Orders> selectByEmployeeSubHandler(Integer employeeId);
    List<Orders> selectOrdersByCustomer(Integer customerId);
    List<Orders> selectOrdersBySubHandlerId(Integer subHandlerId);
    List<Orders> selectActiveOrdersForEmployee();
    List<Orders> selectGotOrdersByEmployeeId(Integer employeeId);
    List<Orders> selectPaidOrdersForEmployee(Integer employeeId);
    List<Orders> selectPaidOrdersForCustomer(Integer employeeId);
    List<Orders> optionalSelectOrders(LocalDate startDate, LocalDate endDate, List<String> handlersName, List<String> subHandlers);
    List<Orders> optionalSelectOrdersForEmployee(Integer employeeId, String orderState);
    List<Orders> optionalSelectOrdersForCustomer(Integer employeeId, String orderState);
}
