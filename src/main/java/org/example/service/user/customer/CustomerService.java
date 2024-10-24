package org.example.service.user.customer;

import jakarta.validation.constraints.NotNull;
import org.example.domain.*;
import org.example.dto.CustomerSignUpDto;
import org.example.dto.OrderDto;
import org.example.dto.PayToCartDto;
import org.example.dto.customer.HandlerCustomerDto;
import org.example.dto.customer.OfferDtoForCustomer;
import org.example.dto.customer.OrdersOutputDtoCustomer;
import org.example.dto.servisesDone.DoneDutiesDto;
import org.example.enumirations.TypeOfUser;
import org.example.service.user.BaseUserService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface CustomerService extends BaseUserService<Customer> {
    CustomerSignUpDto createCustomer(CustomerSignUpDto customerDto);
    boolean validateCustomer(CustomerSignUpDto customerDto);
    OrderDto createOrder(OrderDto orderDto);
    List<OfferDtoForCustomer> getOffersForOrder(Integer orderId);
    void startOrder(Integer orderId);
    boolean checkIfNotDuplicateUser(String user);
    String addComment(Integer ordersId, Integer star, String comment);
    List<OrdersOutputDtoCustomer> getAllOrders(@NotNull Integer customerId);
    void customerAcceptOffer(Integer offerId);
    List<HandlerCustomerDto> getHandlersForCustomer();
    List<SubHandler> getSubHandlersForHandler(Integer handlerId);
    String customerChargeCart(PayToCartDto payToCartDto);
    String makeServiceStateToDone(Integer orderId);
    String customerPay(Integer ordersId, Integer customerId);
    List<Customer> findCustomerByOptional(String name, String lastName, String email, String phone);
    Customer findByEmail(String email);
    void sendToken(String email , TypeOfUser typeOfUser);
    String validateCustomerEmail(String token);
    List<Orders> findPaidOrders(Integer employeeId);
    List<DoneDutiesDto> findDoneWorksById(Integer id);
}
