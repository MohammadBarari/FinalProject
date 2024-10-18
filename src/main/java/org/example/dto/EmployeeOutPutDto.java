package org.example.dto;

public record EmployeeOutPutDto(
        String name,
        String lastName,
        String email,
        String phone,
        String subHandlerName
) {
}
