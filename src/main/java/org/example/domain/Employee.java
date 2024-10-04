package org.example.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.enumirations.EmployeeState;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Employee extends Users {
    @Enumerated(EnumType.STRING)
    private EmployeeState employeeState;

    @Lob
    private byte[] image;

    private Integer score;

    @ManyToMany
    private Set<SubHandler> subHandlers;

    @OneToOne(cascade = CascadeType.ALL)
    private Credit credit;

    public Employee setEmployeeState(EmployeeState employeeState) {
        if (employeeState == null) {
            this.employeeState = EmployeeState.NEW;
        }else {
        this.employeeState = employeeState;
        }
            return this;
    }
}
