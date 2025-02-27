package org.example.dto.customer;

public record SortingOfferInput(Integer orderId, Boolean sortByScore, Boolean ascending) {
}
