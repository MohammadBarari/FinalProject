package org.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Offer extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "employee_Id")
    private Employee employee;
    private LocalDateTime timeOfCreate;
    private Long offerPrice;
    //todo: must be after time that customer set
    @Future
    private LocalDateTime timeOfWork;
    private Integer workTimeInMinutes;
    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Orders orders;
    private boolean accepted;

    @PrePersist
    private void setTimeOfCreate() {
        timeOfCreate = LocalDateTime.now();
    }
}
