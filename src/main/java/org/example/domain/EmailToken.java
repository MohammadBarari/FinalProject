package org.example.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.enumirations.TypeOfUser;

import java.time.LocalDateTime;

@Entity
@Table(name = "token")
@Getter
@Setter
public class EmailToken extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String token;
    private boolean expired;
    private LocalDateTime expiresAt;

    @Enumerated(EnumType.STRING)
    private TypeOfUser typeOfUser;

}
