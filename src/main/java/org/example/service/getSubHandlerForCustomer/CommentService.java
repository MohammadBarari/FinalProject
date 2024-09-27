package org.example.service.getSubHandlerForCustomer;

import org.example.domain.Customer;

public interface CommentService {
    void giveComment(Integer ordersId, int star, String comment);

}
