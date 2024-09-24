package org.example.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.example.domain.Offer}
 */
public record OfferDto(Long offerPrice, LocalDateTime timeOfWork, Integer WorkTimeInMinutes) implements Serializable {
}