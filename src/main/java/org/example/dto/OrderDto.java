package org.example.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.example.domain.Order}
 */
public record OrderDto(@NotNull Double OfferedPrice, String detail, @Future LocalDateTime timeOfWork,
                       String address) implements Serializable {
}