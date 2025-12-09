package org.example.domain;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.enumirations.OrderState;

import java.time.LocalDateTime;
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Orders extends BaseEntity {
    @Column(nullable = false)
    private Double offeredPrice;

    private String detail;

    @ManyToOne(fetch = FetchType.LAZY)
    private SubHandler subHandler;

    private LocalDateTime timeOfWork;

    @NotNull
    private String address;

    @Enumerated(EnumType.STRING)
    private OrderState orderState;

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "customer_id")
    private Customer customer;

    //todo: it doesnt save to database for two initial state of order
    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    //todo: these should be store in database last ones
    private Integer score;

    private String comment;

    public Orders setOrderState(OrderState orderState) {
        if (orderState == null) {
            orderState = OrderState.WAITING_FOR_EMPLOYEE_OFFER;
        }
        this.orderState = orderState;
        return this;
    }
}
