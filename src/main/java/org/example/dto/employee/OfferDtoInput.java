package org.example.dto.employee;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.example.domain.Offer}
 */
public record OfferDtoInput(@NotNull @Digits(integer = 8,fraction = 0)  Long offerPrice,
                            @NotNull LocalDateTime timeOfWork,
                            @Digits(integer = 4,fraction = 0) @NotNull Integer workTimeInMinutes
, @NotNull  Integer orderId) implements Serializable {
}