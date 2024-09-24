package org.example.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.*;
import org.example.enumirations.OrderState;

import java.time.LocalDateTime;
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    private Integer id;
    private Double OfferedPrice;
    private String detail;
    @ManyToOne
    private SubHandler subHandler;
    @Future
    private LocalDateTime timeOfWork;
    private String address;
    private OrderState orderState;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    //todo: it doesnt save to database for two initial state of order
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    //todo: these should be store in database last ones
    private Integer score;
    private String comment;
    //todo: i dont know man i dont know
    //todo: you lost something in yourself dont seek outside just look inside see what are u missing whats makes and change your mood completely
}
