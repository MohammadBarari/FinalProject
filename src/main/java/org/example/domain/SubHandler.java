package org.example.domain;

import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubHandler {
    private String name;
    private String detail;
    private Double BasePrice;
    @ManyToOne
    private Handler handler;
}
