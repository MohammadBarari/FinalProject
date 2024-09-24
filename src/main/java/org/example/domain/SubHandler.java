package org.example.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class SubHandler {
    @Id
    private Integer id;
    private String name;
    private String detail;
    private Double BasePrice;
    @ManyToOne
    private Handler handler;
}
