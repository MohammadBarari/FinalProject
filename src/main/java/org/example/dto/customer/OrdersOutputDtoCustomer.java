package org.example.dto.customer;

import org.example.enumirations.OrderState;

import java.time.LocalDateTime;

public record OrdersOutputDtoCustomer(
        Integer id,
                                      Integer employeeId,
        String employeeName,
        Double offerPrice,
        String detail,
                                      String subHandlerName,
                                      LocalDateTime timeOfWork,
                                      String address,
                                      OrderState orderState
                                      ) {
}
