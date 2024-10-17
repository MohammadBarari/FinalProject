package org.example.dto;

import jakarta.validation.constraints.NotNull;

public record SaveSubHandlerDto(
        @NotNull
        Integer handlerId,
        @NotNull
        String name,
        @NotNull
        String detail,
        @NotNull
        Double basePrice
) {
}
