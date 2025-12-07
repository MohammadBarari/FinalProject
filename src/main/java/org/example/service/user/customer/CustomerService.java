package org.example.service.user.customer;

import org.example.domain.Customer;
import org.example.dto.customer.CustomerSignUpDto;
import org.example.dto.orders.OrderDto;
import org.example.dto.customer.PayToCartDto;
import org.example.dto.admin.CustomerOutputDtoForReport;
import org.example.dto.admin.FindCustomerByFilterDto;
import org.example.dto.admin.FindFilteredCustomerDto;
import org.example.dto.customer.*;
import org.example.dto.servisesDone.DoneDutiesDto;
import org.example.dto.subHandlers.SubHandlersDtoOutputId;
import org.example.dto.user.OrdersOutputDtoUser;
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
    void addComment(Integer ordersId, Integer star, String comment);
    List<OrdersOutputDtoCustomer> getAllOrders( Integer customerId);
    void customerAcceptOffer(Integer offerId);
    List<HandlerCustomerDto> getHandlersForCustomer();
    List<SubHandlersDtoOutputId> getSubHandlersForHandler(Integer handlerId);
    String customerChargeCart(PayToCartDto payToCartDto );
    String makeServiceStateToDone(Integer orderId);
    String customerPay(Integer ordersId, Integer customerId);
    List<Customer> findCustomerByOptional(FindFilteredCustomerDto input);
    Customer findByEmail(String email);
    void sendToken(String email , TypeOfUser typeOfUser);
    String validateCustomerEmail(String token);
    List<DoneDutiesDto> findDoneWorksById(Integer id);
    List<CustomerOutputDtoForReport> findCustomerByReports(FindCustomerByFilterDto input);
    List<OrdersOutputDtoUser> optionalSelectOrdersForCustomer(Integer employeeId, String orderState);
    Double getCreditAmount(Integer id);
    List<SortedOfferDtoForCustomer> sortedOfferForCustomer(SortingOffer input);
    CustomerLoginDtoOutput login(String user, String pass) ;
}
