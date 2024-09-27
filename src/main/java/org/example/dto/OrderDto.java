package org.example.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.example.domain.Orders;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Orders}
 */
public record OrderDto(@NotNull Double OfferedPrice, String detail, @Future LocalDateTime timeOfWork,
                       String address) implements Serializable {
}