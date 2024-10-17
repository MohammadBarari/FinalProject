package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CustomerSignUpDto(
        @NotNull  String name,
        @NotNull  String last_name,
        @NotNull  @Email  String email,
        @NotNull @Pattern(regexp = "^\\d{11}$", message = "Phone number must be exactly 11 digits") String phone ,
        @NotNull  String password) {
}
