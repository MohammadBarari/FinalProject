package org.example.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CustomerCart extends BaseEntity{
    private String CartNumber;
    private String cvv2;
    private LocalDate expiresDate;
    private Double money;
    @OneToOne
    private Customer customer;
}
