package org.example.dto.admin;

import org.example.enumirations.EmployeeState;

import java.time.LocalDateTime;

public record EmployeeOutputDtoReport(Integer id,
                                      String name,
                                      String lastName,
                                      String email,
                                      String phone,
                                      LocalDateTime timeOfRegistration,
                                      EmployeeState employeeState,
                                      Long worksDone,
                                      Long sentOffers) {
}
