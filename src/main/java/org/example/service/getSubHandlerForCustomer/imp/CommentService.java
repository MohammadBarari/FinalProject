package org.example.service.getSubHandlerForCustomer.imp;

import lombok.SneakyThrows;
import org.example.domain.Order;
import org.example.enumirations.OrderState;
import org.example.exeptions.starShouldBeenBetween1To5;
import org.example.service.order.OrderService;
import org.example.service.order.imp.OrderServiceImp;

public class CommentService {
    private OrderService orderService;
    public CommentService() {
        orderService = new OrderServiceImp();
    }
    @SneakyThrows
    public void giveComment(Order order , int star,String comment){
        if (order.getOrderState() == OrderState.PAID){
            if (validateScore(star)){
                order.setScore(star);
            }else {
                throw new starShouldBeenBetween1To5();
            }
            order.setComment(comment);
            orderService.update(order);
        }
    }

    private boolean validateScore(int star) {
        return star <= 5 && star >= 0;
    }
}
