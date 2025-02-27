package org.example.dto.password;

import org.example.enumirations.TypeOfUser;

public record ChangingPasswordDto(
        String user,
        String oldPass,
        String newPass,
        TypeOfUser typeOfUser
) {
}
