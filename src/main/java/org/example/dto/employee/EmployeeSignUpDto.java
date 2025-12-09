package org.example.dto.employee;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record EmployeeSignUpDto(

        @NotNull  String name,

        @NotNull  String last_name,

        @NotNull @Email String email,

        @NotNull  @Pattern(regexp = "^\\d{11}$") String phone ,

        @NotNull   String password

) {
}
