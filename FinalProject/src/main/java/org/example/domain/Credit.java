package org.example.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.enumirations.TypeOfUser;
@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Credit extends BaseEntity{
    private Double amount;

    @Enumerated(EnumType.STRING)
    private TypeOfUser typeOfEmployee;

    public Credit() {
        this.amount = 0.0;
    }
}
