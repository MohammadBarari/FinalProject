package org.example.dto.customer;

import org.example.enumirations.OrderState;

import java.time.LocalDateTime;

public record OrdersOutputDtoCustomer(Double offerPrice,
                                      String detail,
                                      String subHandlerName,
                                      LocalDateTime timeOfWork,
                                      String address,
                                      OrderState orderState
                                      ) {
}
