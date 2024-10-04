package org.example.service.mainService;

import org.example.domain.Offer;
import org.example.exeptions.ErrorWhileFindingOffers;
import org.example.exeptions.NotFoundOrder;
import org.example.exeptions.OrderStateIsNotCorrect;

import java.util.List;

public interface OrderAndCustomerService {
    void changeOrderToStart(Integer orderId) throws NotFoundOrder, OrderStateIsNotCorrect;
    List<Offer> customerSeeAllOfferInOneOrder(Integer orderId) throws ErrorWhileFindingOffers;
}
