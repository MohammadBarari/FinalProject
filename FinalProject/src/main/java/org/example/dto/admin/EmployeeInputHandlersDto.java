package org.example.dto.admin;

import jakarta.validation.constraints.Email;

import java.util.List;

public record EmployeeInputHandlersDto(
        String name,
        String lastName,
        @Email String email,
        String phone,
        List<String> subHandlerName,
        List<String> handlersName,
        Integer minScore,
        Integer maxScore,
        boolean ascending
) {
}
