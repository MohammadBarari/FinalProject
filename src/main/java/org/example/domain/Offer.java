package org.example.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private Order order;
    private boolean accepted;
}
