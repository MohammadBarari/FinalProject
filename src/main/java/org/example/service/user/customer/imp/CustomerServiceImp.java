package org.example.service.user.customer.imp;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.domain.*;
import org.example.dto.CustomerSignUpDto;
import org.example.dto.CustomerSignUpOutPut;
import org.example.dto.OrderDto;
import org.example.enumirations.OrderState;
import org.example.enumirations.TypeOfUser;
import org.example.exeptions.*;
import org.example.repository.user.BaseUserRepository;
import org.example.repository.user.customer.CustomerRepository;
import org.example.service.mainService.imp.CustomerAcceptOfferClass;
import org.example.service.offer.OfferService;
import org.example.service.order.OrderService;
import org.example.service.subHandler.SubHandlerService;
import org.example.service.user.BaseUserServiceImp;
import org.example.service.user.customer.CustomerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
@Service

public class CustomerServiceImp extends BaseUserServiceImp<Customer> implements CustomerService {
    private final CustomerRepository customerRepository ;
    private final SubHandlerService subHandlerService;
    private final OrderService orderService ;
    private final OfferService offerService ;
    private final CustomerAcceptOfferClass customerAcceptOfferClass;

    public CustomerServiceImp(BaseUserRepository baseUserRepository, CustomerRepository customerRepository, SubHandlerService subHandlerService, OrderService orderService, OfferService offerService, CustomerAcceptOfferClass customerAcceptOfferClass) {
        super(baseUserRepository);
        this.customerRepository = customerRepository;
        this.subHandlerService = subHandlerService;
        this.orderService = orderService;
        this.offerService = offerService;
        this.customerAcceptOfferClass = customerAcceptOfferClass;
    }

    @SneakyThrows
    public OrderDto getSubHandlerForCustomer(OrderDto orderDto) {
        Customer customer = findById(orderDto.customerId() , Customer.class);
        if (Objects.isNull(customer)) {
            throw new NotFoundCustomer();
        }
        SubHandler subHandler =  subHandlerService.findSubHandlerById(orderDto.subHandlerId());
        if (Objects.isNull(subHandler)) {
            throw new HandlerIsNull();
        }
        if (isOrderValidated(orderDto,subHandler)) {
            Orders orders = new Orders();
            orders.setCustomer(customer);
            orders.setSubHandler(subHandler);
            orders.setAddress(orderDto.address());
            orders.setDetail(orderDto.detail());
            orders.setOrderState(OrderState.WAITING_FOR_EMPLOYEE_OFFER);
            orders.setOfferedPrice(orderDto.offeredPrice());
            orderService.save(orders);
            return orderDto;
        }
        return null;
    }
    private boolean isOrderValidated(OrderDto orderDto,SubHandler subHandler)
            throws TimeOfWorkDoesntMatch, OrderPriceShouldBeHigherThanBase {
        if (orderDto.timeOfWork().isBefore(LocalDateTime.now())) {
            throw new TimeOfWorkDoesntMatch();
        }
        if (orderDto.offeredPrice() < subHandler.getBasePrice()) {
            throw new OrderPriceShouldBeHigherThanBase();
        }
        return true;
    }

    @SneakyThrows
    public List<Offer> customerSeeAllOfferInOneOrder(Integer orderId)  {
        try {
            return offerService.findAllOffersForSpecificOrder(orderId);
        }catch (Exception e){
            throw new ErrorWhileFindingOffers();
        }
    }
    @SneakyThrows
    public void customerAcceptOffer(Integer offerId){
        customerAcceptOfferClass.customerAcceptOffer(offerId);
    }

    @SneakyThrows
    public List<Orders> getAllOrders(@NotNull Integer customerId){
        List<Orders> customerOrders = orderService.findAllOrdersThatHaveSameCustomer(customerId);
        return customerOrders;
    }
    @SneakyThrows
    public void changeOrderToStart(Integer orderId) {
        Orders order = orderService.findById(orderId);
        if (Objects.isNull(order)){
            throw new NotFoundOrder();
        }
        if (order.getOrderState() != OrderState.UNDER_REACHING_EMPLOYEE){
            throw new OrderStateIsNotCorrect();
        }
        order.setOrderState(OrderState.STARTED);
        orderService.save(order);
    }

    @SneakyThrows
    @Override
    @Transactional
    public CustomerSignUpDto signUpCustomer(CustomerSignUpDto customerDto){
        if (validateCustomer(customerDto)) {
            Customer customer = new Customer();
            //settingCustomer from customerDto and pass it to base for saving
            customer.setName(customerDto.name());
            customer.setLast_name(customerDto.last_name());
            customer.setEmail(customerDto.email());
            customer.setPhone(customerDto.phone());
            customer.setTimeOfRegistration(LocalDateTime.now());
            PassAndUser passAndUser = new PassAndUser();
            passAndUser.setUsername(customerDto.phone());
            passAndUser.setPass(customerDto.password());
            Credit credit = new Credit();
            credit.setTypeOfEmployee(TypeOfUser.CUSTOMER);
            credit.setAmount(0d);
            customer.setCredit(credit);
            passAndUser.setTypeOfUser(TypeOfUser.CUSTOMER);
            customer.setPassAndUser(passAndUser);
            savePassAndUser(passAndUser);
            //setting its value
            signUp(customer);
            return customerDto;
        }
        return null;
    }

    @Override
    public boolean validateCustomer(CustomerSignUpDto customerDto) {
        //todo: validate everything about customer
        if (checkIfNotDuplicateUser(customerDto.phone()) && validatePassWord(customerDto.password())) {
            return true;
        }
        return false;
    }

    @Override
    public Customer login(String user, String pass)
    {
        return customerRepository.login(user,pass);
    }
    @Override
    public boolean checkIfNotDuplicateUser(String user) {
        if (Objects.isNull(customerRepository.find(user,Customer.class)))
        {
            return true;
        }
        return false;
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
                throw new StarShouldBeenBetween1To5();
            }
            orders.setComment(comment);
            orderService.update(orders);
        }else {
            throw new OrderStateIsNotCorrect();
        }
    }
    private boolean validateScore(int star) {
        return star <= 5 && star >= 0;
    }

}
