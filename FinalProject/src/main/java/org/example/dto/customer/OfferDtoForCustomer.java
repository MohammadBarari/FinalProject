package org.example.dto.customer;

import java.time.LocalDateTime;

public record OfferDtoForCustomer(Integer id,
        Long offerPrice,
                                  Integer employeeScore,
                                  String employeeName,
                                  LocalDateTime timeOfWork,
                                  Integer workInMinutes,
                                  boolean accepted) {
}
