package org.example.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String name;
    private String last_name;
    private String email;
    private String phone;
    private LocalDateTime timeOfRegistration;
}