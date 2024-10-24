package org.example.dto.admin;

import org.example.enumirations.EmployeeState;

import java.time.LocalDateTime;
import java.util.List;

public record EmployeeOutputDtoHandlers(Integer id,
                                        String name,
                                        String lastName,
                                        String email,
                                        String phone,
                                        LocalDateTime timeOfRegistration,
                                        EmployeeState employeeState,
                                        Integer score,
                                        List<String> subHandlersName) {
}
