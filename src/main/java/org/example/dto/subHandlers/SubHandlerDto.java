package org.example.dto.subHandlers;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

public record SubHandlerDto(
        @NotNull
        Integer handlerId,
        @NotNull
        String name,
        @NotNull
        String detail,
        @NotNull
        @Digits(integer = 10, fraction = 2)
        Double basePrice
) {
}
