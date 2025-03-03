package org.example.service.offer;

import org.example.domain.Employee;
import org.example.domain.Offer;

import java.util.List;

public interface OfferService {
    void save(Offer offer);
    void update(Offer offer);
    void delete(Integer offerId);
    List<Offer> findAllOffersForSpecificOrder(Integer orderId);
    Offer findById(int id);
    Offer findAcceptedOfferInOrder(Integer id);
    Employee findEmployeeByOfferId(int id);
}
