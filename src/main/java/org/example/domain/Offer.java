package org.example.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "employee_Id")
    private Employee employee;
    private LocalDateTime timeOfCreate;
    private Long offerPrice;
    //todo: must be after time that customer set
    private LocalDateTime timeOfWork;
    private Integer WorkTimeInMinutes;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders orders;
    private boolean accepted;
}
