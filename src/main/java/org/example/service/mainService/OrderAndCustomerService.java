package org.example.service.mainService;

import org.example.domain.Offer;
import org.example.exeptions.NotFoundException.NotFoundOrder;
import org.example.exeptions.order.OrderStateIsNotCorrect;

import java.util.List;

public interface OrderAndCustomerService {
    void changeOrderToStart(Integer orderId);
    List<Offer> customerSeeAllOfferInOneOrder(Integer orderId);
}
