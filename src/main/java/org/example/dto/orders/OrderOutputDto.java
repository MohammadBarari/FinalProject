package org.example.dto.orders;

import org.example.enumirations.OrderState;

import java.time.LocalDateTime;

public record OrderOutputDto(Double offerPrice,
                             String detail,
                             String subHandlerName,
                             LocalDateTime timeOfWork,
                             String address,
                             OrderState orderState,
                             String customerName,
                             Integer customerId,
                             String employeeName,
                             Integer employeeId,
                             Integer score,
                             String comment) {
}
