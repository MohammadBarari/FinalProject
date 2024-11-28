package org.example.dto.customer;

public record SortingOffer(Integer customerId, Integer orderId, Boolean sortByScore, Boolean ascending) {
}
