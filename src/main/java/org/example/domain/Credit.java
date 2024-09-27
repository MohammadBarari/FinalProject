package org.example.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.enumirations.TypeOfUser;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private Double amount;
    private TypeOfUser typeOfEmployee;
}
