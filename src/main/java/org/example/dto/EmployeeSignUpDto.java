package org.example.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.awt.*;

public record EmployeeSignUpDto(
                                @NotNull  String name,
                                @NotNull  String last_name,
                                @NotNull @Email String email,
                                @NotNull  @Pattern(regexp = "^\\d{10}$") String phone ,
                                @NotNull  String password,
                                @NotNull  String imagePath) {
}
