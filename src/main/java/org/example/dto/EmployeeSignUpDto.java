package org.example.dto;


import java.awt.*;

public record EmployeeSignUpDto(String name, String last_name, String email, String phone , String password,
                                String imagePath) {
}
