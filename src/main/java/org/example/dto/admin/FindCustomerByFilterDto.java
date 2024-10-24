package org.example.dto.admin;

import java.time.LocalDate;

public record FindCustomerByFilterDto(LocalDate startDate,
                                      LocalDate endDate,
                                      Integer doneOrderStart,
                                      Integer doneOrderEnd) {
}
