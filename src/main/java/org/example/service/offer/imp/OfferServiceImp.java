package org.example.service.offer.imp;

import org.example.domain.Offer;
import org.example.service.offer.OfferService;

import java.util.List;

public class OfferServiceImp implements OfferService {
    private final OfferRepository offerRepository;

    public OfferServiceImp() {
        offerRepository = new OfferRepositoryImp();
    }

    @Override
    public void save(Offer offer) {
        offerRepository.save(offer);
    }

    @Override
    public void update(Offer offer) {
        offerRepository.update(offer);
    }

    @Override
    public void delete(Integer offerId) {
        offerRepository.delete(offerId);
    }

    @Override
    public List<Offer> findAllOffersForSpecificOrder(Integer orderId) {
       return offerRepository.selectById(orderId);
    }

    @Override
    public List<Offer> findAll() {
        return List.of();
    }
}