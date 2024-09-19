package org.example.domain;

import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.enumirations.EmployeeState;

import java.awt.*;
import java.util.Set;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends User{
    private EmployeeState employeeState;

    private EmployeeImages employeeImage;

    private Integer score;

    @ManyToMany
    private Set<SubHandler> subHandlers;
}
