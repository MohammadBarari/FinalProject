package org.example.dto;

public record changingPasswordDtoController(
        String user,
        String oldPass,
        String newPass
) {
}
