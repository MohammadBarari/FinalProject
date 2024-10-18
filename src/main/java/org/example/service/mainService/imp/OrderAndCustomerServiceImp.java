package org.example.service.mainService.imp;

import org.example.domain.Offer;
import org.example.domain.Orders;
import org.example.enumirations.OrderState;
import org.example.exeptions.ErrorWhileFindingOffers;
import org.example.exeptions.NotFoundOrder;
import org.example.exeptions.OrderStateIsNotCorrect;
import org.example.service.mainService.OrderAndCustomerService;
import org.example.service.offer.OfferService;
import org.example.service.order.OrderService;
import org.hibernate.query.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OrderAndCustomerServiceImp implements OrderAndCustomerService {
    private final OrderService orderService;
    private final OfferService offerService;
    public OrderAndCustomerServiceImp(OrderService orderService, OfferService offerService) {
        this.orderService = orderService;
        this.offerService = offerService;
    }

    @Override
    //todo:to be deleted
    public void changeOrderToStart(Integer orderId) throws NotFoundOrder, OrderStateIsNotCorrect {
        Orders order = orderService.findById(orderId);
        if (Objects.isNull(order)){
            throw new NotFoundOrder();
        }
        if (order.getOrderState() != OrderState.UNDER_REACHING_EMPLOYEE){
            throw new OrderStateIsNotCorrect();
        }
        order.setOrderState(OrderState.STARTED);
        orderService.update(order);
    }
//todo: should be deleted
    @Override
    public List<Offer> customerSeeAllOfferInOneOrder(Integer orderId) throws ErrorWhileFindingOffers {
        try {
            return offerService.findAllOffersForSpecificOrder(orderId);
        }catch (Exception e){
            throw new ErrorWhileFindingOffers();
        }
    }
}
