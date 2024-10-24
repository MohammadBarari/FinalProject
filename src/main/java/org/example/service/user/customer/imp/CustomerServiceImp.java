package org.example.service.user.customer.imp;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import org.example.domain.*;
import org.example.dto.CustomerSignUpDto;
import org.example.dto.OrderDto;
import org.example.dto.PayToCartDto;
import org.example.dto.admin.CustomerOutputDtoForReport;
import org.example.dto.customer.HandlerCustomerDto;
import org.example.dto.customer.OfferDtoForCustomer;
import org.example.dto.customer.OrdersOutputDtoCustomer;
import org.example.dto.employee.SubHandlerOutput;
import org.example.dto.servisesDone.DoneDutiesDto;
import org.example.dto.subHandlers.SubHandlersDtoOutput;
import org.example.enumirations.OrderState;
import org.example.enumirations.TypeOfUser;
import org.example.exeptions.*;
import org.example.repository.user.BaseUserRepository;
import org.example.repository.user.customer.CustomerRepository;
import org.example.service.credit.CreditService;
import org.example.service.customerCart.CustomerCartService;
import org.example.service.emailToken.EmailTokenService;
import org.example.service.handler.HandlerService;
import org.example.service.mainService.imp.CustomerAcceptOfferClass;
import org.example.service.mainService.imp.CombinedUserClassFromCustomer;
import org.example.service.mapStruct.EntityMapper;
import org.example.service.offer.OfferService;
import org.example.service.order.OrderService;
import org.example.service.subHandler.SubHandlerService;
import org.example.service.user.BaseUserServiceImp;
import org.example.service.user.customer.CustomerService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CustomerServiceImp extends BaseUserServiceImp<Customer> implements CustomerService {
    private final CustomerRepository customerRepository ;
    private final HandlerService handlerService;
    private final CreditService creditService;
    private final CustomerCartService customerCartService;
    private final CombinedUserClassFromCustomer combineUserClass;
    public CustomerServiceImp(BaseUserRepository baseUserRepository, EmailTokenService emailTokenService, EntityMapper entityMapper, CombinedUserClassFromCustomer combineUserClass, CustomerCartService customerCartService , CreditService creditService, CustomerRepository customerRepository, SubHandlerService subHandlerService, OrderService orderService, OfferService offerService, CustomerAcceptOfferClass customerAcceptOfferClass, HandlerService handlerService) {
        super(baseUserRepository,orderService,offerService,subHandlerService,entityMapper,emailTokenService);
        this.customerRepository = customerRepository;
        this.handlerService = handlerService;
        this.creditService = creditService;
        this.customerCartService = customerCartService;
        this.combineUserClass = combineUserClass;
    }


    public OrderDto createOrder(OrderDto orderDto) {
        Customer customer = findCustomer(orderDto.customerId());
        SubHandler subHandler = findSubHandler(orderDto.subHandlerId());

        if (isOrderValidated(orderDto,subHandler)) {
            return buildOrder(orderDto,customer, subHandler);
        }
        return null;
    }
    private void validateOrder(OrderDto orderDto, SubHandler subHandler) {
        if (orderDto.timeOfWork().isBefore(LocalDateTime.now())) {
            throw new TimeOfWorkDoesntMatch();
        }
        if (orderDto.offeredPrice() < subHandler.getBasePrice()) {
            throw new OrderPriceShouldBeHigherThanBase();
        }
    }

    private OrderDto buildOrder(OrderDto orderDto, Customer customer, SubHandler subHandler) {
        Orders orders = entityMapper.dtoToOrder(orderDto);
        orders.setOrderState(OrderState.WAITING_FOR_EMPLOYEE_OFFER);
        orders.setCustomer(customer);
        orders.setSubHandler(subHandler);
        orderService.save(orders);
        return orderDto;
    }
    private Customer findCustomer(Integer customerId) throws NotFoundCustomer {
        return Optional.ofNullable(customerRepository.findById(customerId, Customer.class))
                .orElseThrow(() -> new NotFoundCustomer("Customer not found with ID: " + customerId));
    }

    private SubHandler findSubHandler(Integer subHandlerId) {
        return Optional.ofNullable(subHandlerService.findSubHandlerById(subHandlerId))
                .orElseThrow(() -> new HandlerIsNull("SubHandler not found with ID: " + subHandlerId));
    }
    private boolean isOrderValidated(OrderDto orderDto,SubHandler subHandler) {
        if (orderDto.timeOfWork().isBefore(LocalDateTime.now())) {
            throw new TimeOfWorkDoesntMatch();
        }
        if (orderDto.offeredPrice() < subHandler.getBasePrice()) {
            throw new OrderPriceShouldBeHigherThanBase();
        }
        return true;
    }


    public List<OfferDtoForCustomer> getOffersForOrder(Integer orderId)  {
             List<Offer> offers = offerService.findAllOffersForSpecificOrder(orderId);
             List<OfferDtoForCustomer> offerDtoForCustomers = new ArrayList<>();
             offers.forEach(offer -> {
                 offerDtoForCustomers.add(new OfferDtoForCustomer(offer.getOfferPrice(),offer.getEmployee().getScore(),offer.getEmployee().getName() + " " + offer.getEmployee().getLast_name(),offer.getTimeOfWork(),offer.getWorkTimeInMinutes()));
             });
             return offerDtoForCustomers;
    }

    public void customerAcceptOffer(Integer offerId){
        combineUserClass.acceptOffer(offerId);
    }

    @Override
    public List<HandlerCustomerDto> getHandlersForCustomer() {
         List<Handler> handlers =  handlerService.findAllHandlers();
         List<HandlerCustomerDto> handlerCustomerDtos = new ArrayList<>();
         handlers.forEach(handler -> {

            handlerCustomerDtos.add(new HandlerCustomerDto(handler.getId(),handler.getName()));
         });
         return handlerCustomerDtos;
    }

    @Override
    public List<SubHandler> getSubHandlersForHandler(Integer handlerId) {
        return subHandlerService.findAllSubHandlerSameHandler(handlerId);
    }

    @SneakyThrows
    public List<OrdersOutputDtoCustomer> getAllOrders(@NotNull Integer customerId){
        List<Orders> customerOrders = orderService.findAllOrdersThatHaveSameCustomer(customerId);
        List<OrdersOutputDtoCustomer> ordersOutputDtoCustomers = new ArrayList<>();
        customerOrders.forEach(orders -> {
            ordersOutputDtoCustomers.add(new OrdersOutputDtoCustomer(orders.getOfferedPrice(),orders.getDetail(),orders.getSubHandler().getName(),orders.getTimeOfWork(),orders.getAddress(),orders.getOrderState()));
        });
        return ordersOutputDtoCustomers;
    }
    @SneakyThrows
    public void startOrder(Integer orderId) {
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


    @Override
    @Transactional
    public CustomerSignUpDto createCustomer(CustomerSignUpDto customerDto){
        if (validateCustomer(customerDto)) {
            Customer customer = entityMapper.dtoToCustomer(customerDto);
            customer.setTimeOfRegistration(LocalDateTime.now());
            addCreditAndPass(customer,customerDto);
            sendToken(customerDto.email(),TypeOfUser.CUSTOMER);
            signUp(customer);
            return customerDto;
        }
        else {
            throw new InavlidCustomerDataException("invalid customer data for sign up");
        }
    }
    private void addCreditAndPass(Customer customer,CustomerSignUpDto customerDto){
        PassAndUser passAndUser = getPassAndUser(customerDto);
        Credit credit = new Credit();
        credit.setTypeOfEmployee(TypeOfUser.CUSTOMER);
        credit.setAmount(0d);
        customer.setCredit(credit);
        customer.setPassAndUser(passAndUser);
    }
    private static PassAndUser getPassAndUser(CustomerSignUpDto customerDto) {
        PassAndUser passAndUser = new PassAndUser();
        passAndUser.setUsername(customerDto.phone());
        passAndUser.setPass(customerDto.password());
        passAndUser.setTypeOfUser(TypeOfUser.CUSTOMER);
        return passAndUser;
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
        Customer customer =  customerRepository.login(user,pass);
        if (customer != null)
        {
            return customer;
        }
        throw new NotFoundCustomer();
    }
    @Override
    public boolean checkIfNotDuplicateUser(String user) {
        return Objects.isNull(customerRepository.find(user, Customer.class));
    }
    public String addComment(Integer ordersId, Integer star, String comment){
        Orders orders = findOrder(ordersId);
        validateForComment(orders,star);
        orders.setComment(comment);
        orderService.update(orders);
        return "successful";
    }
    private void validateForComment(Orders orders,Integer star){
        if (!(orders.getOrderState() == OrderState.PAID)){
            throw new OrderStateIsNotCorrect();
        }
        if (!validateStar(star)){
            throw new StarShouldBeenBetween1To5();
        }
    }
    private Orders findOrder(Integer orderId){
        return Optional.ofNullable(orderService.findById(orderId)).orElseThrow(() -> new NotFoundOrder("Order not found with ID: " + orderId));
    }
    private boolean validateStar(int star) {
        return star <= 5 && star >= 0;
    }
    @Override
    @Transactional
    public String customerChargeCart(PayToCartDto payToCartDto){
        Credit customerCredit = creditService.findByCustomerId(payToCartDto.customerId());
        CustomerCart customerCart = customerCartService.findCustomerCartByCustomerId(payToCartDto.customerId());
        if (Objects.isNull(customerCart)){
            customerCart = getCustomerCart(payToCartDto);
        }
        if (customerCart.getMoney() - payToCartDto.amount() >0) {
            customerCredit.setAmount(customerCredit.getAmount() + payToCartDto.amount());
            creditService.update(customerCredit);
            customerCart.setMoney(customerCart.getMoney() - payToCartDto.amount());
            customerCartService.updateCustomerCart(customerCart);
            return "successful";
        }else {
            throw new DontHaveEnoughMoney();
        }
    }

    private CustomerCart getCustomerCart(PayToCartDto payToCartDto) {
        CustomerCart customerCart;
        customerCart  = new CustomerCart();
        customerCart.setCartNumber(payToCartDto.cartNumber());
        customerCart.setExpiresDate(payToCartDto.expiresDate());
        customerCart.setCvv2(payToCartDto.cvv2());
        Customer customer = new Customer();
        customer.setId(payToCartDto.customerId());
        customerCart.setCustomer(customer);
        customerCart.setMoney(payToCartDto.amount() + 1000d);
        return customerCart;
    }

    @Override
    public String makeServiceStateToDone(Integer orderId) {
            combineUserClass.makeTheOrderDone(orderId);
            System.out.println("what happened");
            return "successful";
    }
    @Transactional
    @Override
    public String customerPay(Integer ordersId, Integer customerId){
            Orders orders = findOrder(ordersId);
            Customer customer = findCustomer(customerId);
            Offer offer = Optional.ofNullable(offerService.findAcceptedOfferInOrder(ordersId)).orElseThrow(() -> new NotFoundOffer("Offer not found any accepted offer with orderID: " + ordersId));
            if (validateCustomerCanPay(orders, customer, offer)) {
                Credit cusromerCredit = creditService.findCreditById(customer.getCredit().getId());
                Credit employeeCredit = creditService.findByEmployeeId(offer.getEmployee().getId());
                payToEmployeeCredit(cusromerCredit, offer, employeeCredit, orders);
                return "successful";
            }else {
               throw new FailedDoingOperation("Customer payment failed");
            }
    }

    private void payToEmployeeCredit(Credit cusromerCredit, Offer offer, Credit employeeCredit, Orders orders) {
        cusromerCredit.setAmount(cusromerCredit.getAmount() - offer.getOfferPrice());
        creditService.update(cusromerCredit);
        employeeCredit.setAmount(employeeCredit.getAmount() +  ((double) 70 / 100) * offer.getOfferPrice());
        creditService.update(employeeCredit);
        orders.setOrderState(OrderState.PAID);
        orderService.update(orders);
    }

    @Override
    public List<Customer> findCustomerByOptional(String name, String lastName, String email, String phone) {
        return customerRepository.selectCustomerByOptional(name, lastName, email, phone);
    }



    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public boolean validateCustomerCanPay(Orders orders, Customer customer, Offer offer) throws DontHaveEnoughMoney {
        if(customer.getCredit().getAmount() > offer.getOfferPrice() && orders.getOrderState() == OrderState.DONE){
            return true;
        }else {
            throw new DontHaveEnoughMoney();
        }
    }
    @Override
    public void sendToken(String email , TypeOfUser typeOfUser) {
        emailTokenService.sendEmail(email,typeOfUser);
    }
    @Override
    @Transactional
    public String validateCustomerEmail(String token) {
        EmailToken emailToken = Optional.ofNullable(emailTokenService.findByToken(token)).orElseThrow(()-> new InvalidTokenExceptions("not found token"));
        emailTokenService.validateToken(token);
        validateCustomerToken(emailToken);
        return "successful";
    }

    private void validateCustomerToken(EmailToken emailToken) {
        Customer customer = Optional.ofNullable(findByEmail(emailToken.getEmail())).orElseThrow(() -> new InvalidTokenExceptions("The token is not from the user"));
        customer.setActive(true);
        emailToken.setExpired(true);
    }
    @Override
    public List<Orders> findPaidOrders(Integer customerId) {
        return orderService.findPaidOrdersForCustomer(customerId);
    }

    @Override
    public List<DoneDutiesDto> findDoneWorksById(Integer id) {
        List<DoneDutiesDto> doneDutiesDtos = new ArrayList<>();
        List<Orders> allDoneOrders = orderService.findPaidOrdersForCustomer(id);
        for (Orders orders : allDoneOrders) {
            Offer offer = offerService.findAcceptedOfferInOrder(orders.getId());
            Integer employeeId = offer.getEmployee().getId();
            String employeeName = offer.getEmployee().getName() + " " +offer.getEmployee().getLast_name();
            Customer customer = customerRepository.findById(orders.getCustomer().getId(), Customer.class);
            Integer customerId = customer.getId();
            String customerFullName = customer.getName() + " " + customer.getLast_name();
            Double price = Double.valueOf(offer.getOfferPrice());
            DoneDutiesDto dto = new DoneDutiesDto(orders.getTimeOfWork(),price,new SubHandlersDtoOutput(orders.getSubHandler().getName(),orders.getSubHandler().getDetail(),orders.getSubHandler().getBasePrice()),orders.getScore(),employeeId,employeeName,customerFullName,customerId,orders.getComment());
            doneDutiesDtos.add(dto);
        }
        return doneDutiesDtos;
    }

    @Override
    public List<CustomerOutputDtoForReport> findCustomerByReports(LocalDate startDate, LocalDate endDate, Integer doneOrderStart, Integer doneOrderEnd) {
        return customerRepository.selectCustomerByReports(startDate,endDate,doneOrderStart,doneOrderEnd);
    }
}
