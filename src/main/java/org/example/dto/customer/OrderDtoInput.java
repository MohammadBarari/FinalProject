package org.example.dto.customer;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.example.domain.Orders;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Orders}
 */
public record OrderDtoInput(
        @NotNull Double offeredPrice,
                       String detail,
                       @Future LocalDateTime timeOfWork,
                       @NotNull String address ,
                       @NotNull Integer subHandlerId) implements Serializable {
}