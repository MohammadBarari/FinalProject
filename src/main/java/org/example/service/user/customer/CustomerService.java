package org.example.service.user.customer;

import jakarta.validation.constraints.NotNull;
import org.example.domain.*;
import org.example.dto.CustomerSignUpDto;
import org.example.dto.OrderDto;
import org.example.service.user.BaseUserService;

import java.util.List;

public interface CustomerService extends BaseUserService<Customer> {
    CustomerSignUpDto signUpCustomer(CustomerSignUpDto customerDto);
    boolean validateCustomer(CustomerSignUpDto customerDto);
    OrderDto getSubHandlerForCustomer(OrderDto orderDto);
    List<Offer> customerSeeAllOfferInOneOrder(Integer orderId);
    void changeOrderToStart(Integer orderId);
    boolean checkIfNotDuplicateUser(String user);
    void giveComment(Integer ordersId, int star, String comment);
    List<Orders> getAllOrders(@NotNull Integer customerId);
    void customerAcceptOffer(Integer offerId);
    List<Handler> customerSeeAllHandlers();
    List<SubHandler> findAllSubHandlerForHandler(Integer handlerId);

}
