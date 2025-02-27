package org.example.dto.user;

import org.example.enumirations.OrderState;

import java.time.LocalDateTime;

public record OrdersOutputDtoUser(Integer orderId,
        Double offerPrice,
                                  String detail,
                                  LocalDateTime timeOfWork,
                                  String address,
                                  OrderState orderState,
                                  Integer score,
                                  String comment) {
}
