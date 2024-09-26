package org.example.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.example.enumirations.TypeOfEmployee;
import org.example.enumirations.TypeOfUser;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Credit {
    @Id
    private int id;
    private Double amount;
    private TypeOfUser typeOfEmployee;
}
