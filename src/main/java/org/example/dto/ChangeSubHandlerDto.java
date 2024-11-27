package org.example.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link org.example.domain.SubHandler}
 */
public record ChangeSubHandlerDto(
        @Digits(integer = 5,fraction = 0) @NotNull  Integer id, String detail,    Double basePrice) implements Serializable {
}