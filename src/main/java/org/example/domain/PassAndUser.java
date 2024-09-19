package org.example.domain;

import lombok.*;
import org.example.enumirations.TypeOfUser;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassAndUser {
    private String pass;
    private String username;
    private TypeOfUser typeOfUser;
}
