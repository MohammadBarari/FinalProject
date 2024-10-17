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
public class SubHandler extends BaseEntity {
    @NotNull
    @Column(unique = true)
    private String name;
    @NotNull
    private String detail;
    @NotNull
    private Double basePrice;
    @ManyToOne(cascade = { CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Handler handler;
}
