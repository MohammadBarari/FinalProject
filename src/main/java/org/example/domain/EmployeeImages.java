package org.example.domain;

import lombok.*;

import java.awt.*;
import java.io.File;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmployeeImages {
    private File file;
    private Image image;
}
