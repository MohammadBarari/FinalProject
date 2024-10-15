package org.example.service.offer.imp;

import lombok.RequiredArgsConstructor;
import org.example.domain.Offer;
import org.example.repository.offer.OfferRepository;
import org.example.service.offer.OfferService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class OfferServiceImp implements OfferService {
    private final OfferRepository offerRepository;


    @Override
    @Transactional
    public void save(Offer offer) {
        offerRepository.save(offer);
    }

    @Override
    @Transactional
    public void update(Offer offer) {
        offerRepository.update(offer);
    }

    @Override
    @Transactional
    public void delete(Integer offerId) {
        offerRepository.delete(offerId);
    }

    @Override
    public List<Offer> findAllOffersForSpecificOrder(Integer orderId) {
       return offerRepository.findAllForOrder(orderId);
    }

    @Override
    public Offer findById(int id) {
        return offerRepository.findById(id);
    }

    @Override
    public Offer findAcceptedOfferInOrder(Integer id) {
        return offerRepository.selectAcceptedOfferInOrder(id);
    }
}
