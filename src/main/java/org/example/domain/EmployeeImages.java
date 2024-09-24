package org.example.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.awt.*;
import java.io.File;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Embeddable
public class EmployeeImages {
    private File file;
    private Image image;
}
