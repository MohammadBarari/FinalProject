package org.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Users extends BaseEntity {
    @NotNull
    @Column(length = 20)
    private String name;
    @NotNull
    @Column(length = 20)
    private String last_name;
    @Email
    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false,unique = true,length = 11)
    private String phone;
    private LocalDateTime timeOfRegistration;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonIgnore
    private PassAndUser passAndUser;
}