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
public class PassAndUser extends BaseEntity {
    @Column(nullable = false)
    private String pass;
    private String username;
    @Enumerated(EnumType.STRING)
    private TypeOfUser typeOfUser;
}
