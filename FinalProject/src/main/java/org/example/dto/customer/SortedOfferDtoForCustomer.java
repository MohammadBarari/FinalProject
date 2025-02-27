package org.example.dto.customer;

import java.time.LocalDateTime;

public record SortedOfferDtoForCustomer(Long offerPrice,
                                        Integer employeeScore,
                                        String employeeName,
                                        LocalDateTime timeOfWork,
                                        Integer workInMinutes,
                                        Boolean ascending,
                                        Boolean sortByScore) {
}
