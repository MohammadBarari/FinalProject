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
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private Double OfferedPrice;

    private String detail;
    @ManyToOne
    private SubHandler subHandler;
    @Future
    private LocalDateTime timeOfWork;
    private String address;
    @Enumerated(EnumType.STRING)
    private OrderState orderState;
    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private Customer customer;
    //todo: it doesnt save to database for two initial state of order
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "employee_id")
    private Employee employee;
    //todo: these should be store in database last ones
    private Integer score;
    private String comment;
}
