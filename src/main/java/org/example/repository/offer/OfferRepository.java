package org.example.repository.offer;

import org.example.domain.Offer;

import java.util.List;

public interface OfferRepository {
    void save(Offer offer);
    void update(Offer offer);
    void delete(int id);
    Offer findById(int id);
    List<Offer> findAllForOrder(int orderId);
}
