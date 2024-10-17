package org.example.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link org.example.domain.SubHandler}
 */
public record ChangeSubHandlerDto(
        @Digits(integer = 3,fraction = 0) @NotNull  Integer id, String detail,  @Digits(integer = 3,fraction = 0)   Double basePrice) implements Serializable {
}