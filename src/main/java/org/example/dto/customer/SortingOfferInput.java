package org.example.dto.customer;

public record SortingOfferInput(Integer customerId,Integer orderId, Boolean sortByScore, Boolean ascending) {
}
