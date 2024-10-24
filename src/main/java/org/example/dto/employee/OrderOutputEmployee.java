package org.example.dto.employee;

import org.example.enumirations.OrderState;

import java.time.LocalDateTime;
public record OrderOutputEmployee(
        Double offerPrice,
        String detail,
        String subHandlerName,
        LocalDateTime timeOfWork,
        String address,
        OrderState orderState,
        String customerName,
        Integer customerId
) {
}
