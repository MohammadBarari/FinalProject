package org.example.service.getSubHandlerForCustomer.imp;

import lombok.SneakyThrows;
import org.example.domain.Orders;
import org.example.enumirations.OrderState;
import org.example.exeptions.NotFoundOrder;
import org.example.exeptions.starShouldBeenBetween1To5;
import org.example.service.order.OrderService;
import org.example.service.order.imp.OrderServiceImp;
import org.hibernate.query.Order;

public class CommentService {
    private OrderService orderService;
    public CommentService() {
        orderService = new OrderServiceImp();
    }
    @SneakyThrows
    public void giveComment(Integer ordersId, int star, String comment){
        Orders orders = orderService.findById(ordersId);
        if (orders == null){
            throw new NotFoundOrder();
        }
        if (orders.getOrderState() == OrderState.PAID){
            if (validateScore(star)){
                orders.setScore(star);
            }else {
                throw new starShouldBeenBetween1To5();
            }
            orders.setComment(comment);
            orderService.update(orders);
        }
    }

    private boolean validateScore(int star) {
        return star <= 5 && star >= 0;
    }
}
