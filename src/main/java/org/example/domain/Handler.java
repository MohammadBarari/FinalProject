package org.example.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Handler extends BaseEntity {
    @NotNull
    @Column(nullable = false, unique = true)
    private String name;
}
