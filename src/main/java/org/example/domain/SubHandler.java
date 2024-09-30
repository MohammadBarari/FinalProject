package org.example.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class SubHandler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Column(unique = true)
    private String name;
    @NotNull
    private String detail;
    @NotNull
    private Double basePrice;
    @ManyToOne(cascade = { CascadeType.MERGE})
    private Handler handler;
}
