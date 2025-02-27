package org.example.domain;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.enumirations.EmployeeState;

import java.util.Objects;
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
        this.employeeState = Objects.requireNonNullElse(employeeState, EmployeeState.NEW);
            return this;
    }
}
