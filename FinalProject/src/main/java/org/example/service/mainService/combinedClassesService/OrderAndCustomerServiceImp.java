package org.example.service.mainService.combinedClassesService;
import org.example.domain.Offer;
import org.example.domain.Orders;
import org.example.enumirations.OrderState;
import org.example.exeptions.NotFoundException.NotFoundOffer;
import org.example.exeptions.NotFoundException.NotFoundOrder;
import org.example.exeptions.order.OrderStateIsNotCorrect;
import org.example.service.offer.OfferService;
import org.example.service.order.OrderService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
public class OrderAndCustomerServiceImp  {
    private final OrderService orderService;
    private final OfferService offerService;
    public OrderAndCustomerServiceImp(OrderService orderService, OfferService offerService) {
        this.orderService = orderService;
        this.offerService = offerService;
    }


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


    public List<Offer> customerSeeAllOfferInOneOrder(Integer orderId)  {
        try {
            return offerService.findAllOffersForSpecificOrder(orderId);
        }catch (Exception e){
            throw new NotFoundOffer();
        }
    }
}
