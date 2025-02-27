package org.example.dto.admin;

import java.time.LocalDate;

public record FindFilteredEmployeeDto(LocalDate startDateRegistration,
                                      LocalDate endDateRegistration,
                                      Integer doneWorksStart,
                                      Integer doneWorksEnd,
                                      Integer offerSentStart,
                                      Integer  offerSentEnd) {
}
