package org.example.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.enumirations.TypeOfUser;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
public class PassAndUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String pass;
    private String username;
    @Enumerated(EnumType.STRING)
    private TypeOfUser typeOfUser;
}
