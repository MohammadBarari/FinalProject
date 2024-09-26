package org.example.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class User {
    @Id
    private Integer id;
    private String name;
    private String last_name;
    private String email;
    private String phone;
    private LocalDateTime timeOfRegistration;
}