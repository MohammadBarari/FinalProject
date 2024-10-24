package org.example.dto.admin;

import org.example.enumirations.OrderState;

import java.time.LocalDateTime;

public record CustomerOutputDtoForReport(Integer id,
                                         String name,
                                         String last_name,
                                         String email,
                                         String phone,
                                         LocalDateTime timeOfRegistration,
                                         Boolean isActive,
                                         Long paidOrdersCount
) {

}
