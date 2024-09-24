package org.example.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.enumirations.EmployeeState;

import java.awt.*;
import java.io.File;
import java.util.Set;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Employee extends User{
    private EmployeeState employeeState;

    @Embedded
    private EmployeeImages employeeImage;


    private Integer score;

    @ManyToMany
    private Set<SubHandler> subHandlers;

    @OneToOne
    private Credit credit;
}
