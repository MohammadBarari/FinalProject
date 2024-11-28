package org.example.dto.password;

public record changingPasswordDtoController(
        String user,
        String oldPass,
        String newPass
) {
}
