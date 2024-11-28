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
public class Offer extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "employee_Id")
    private Employee employee;

    private LocalDateTime timeOfCreate;

    private Long offerPrice;
    //todo: must be after time that customer set
    //todo: changing it because in update it cause propblem have to set them manualy
    private LocalDateTime timeOfWork;

    private Integer workTimeInMinutes;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders orders;
    private boolean accepted;

    @PrePersist
    private void setTimeOfCreate() {
        timeOfCreate = LocalDateTime.now();
    }
}
