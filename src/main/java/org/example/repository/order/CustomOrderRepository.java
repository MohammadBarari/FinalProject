package org.example.repository.order;

import org.example.domain.Orders;

import java.time.LocalDate;
import java.util.List;

public interface CustomOrderRepository {

    List<Orders> optionalSelectOrders(LocalDate startDate, LocalDate endDate, List<String> handlersName, List<String> subHandlers);

    List<Orders> optionalSelectOrdersForEmployee(Integer employeeId, String orderState);

    List<Orders> optionalSelectOrdersForCustomer(Integer employeeId, String orderState);
}
