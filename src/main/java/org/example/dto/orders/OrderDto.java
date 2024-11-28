package org.example.dto.orders;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.example.domain.Orders;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Orders}
 */
public record OrderDto(
        @NotNull Double offeredPrice,
                       String detail,
                       @Future LocalDateTime timeOfWork,
                       @NotNull String address ,
                       @NotNull Integer subHandlerId,
                       @NotNull Integer customerId) implements Serializable {
}