package org.example.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
public class Handler extends BaseEntity {
    @NotNull
    @Column(nullable = false, unique = true)
    private String name;
}
