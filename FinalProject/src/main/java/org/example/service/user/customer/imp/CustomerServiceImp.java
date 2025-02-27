package org.example.service.user.customer.imp;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.example.domain.*;
import org.example.dto.customer.CustomerSignUpDto;
import org.example.dto.orders.OrderDto;
import org.example.dto.customer.PayToCartDto;
import org.example.dto.admin.CustomerOutputDtoForReport;
import org.example.dto.admin.FindCustomerByFilterDto;
import org.example.dto.admin.FindFilteredCustomerDto;
import org.example.dto.customer.*;
import org.example.dto.servisesDone.DoneDutiesDto;
import org.example.dto.subHandlers.SubHandlersDtoOutput;
import org.example.dto.subHandlers.SubHandlersDtoOutputId;
import org.example.dto.user.OrdersOutputDtoUser;
import org.example.enumirations.OrderState;
import org.example.enumirations.TypeOfUser;
import org.example.exeptions.NotFoundException.NotFoundCustomer;
import org.example.exeptions.NotFoundException.NotFoundHandler;
import org.example.exeptions.NotFoundException.NotFoundOffer;
import org.example.exeptions.NotFoundException.NotFoundOrder;
import org.example.exeptions.captcha.CaptchaDoesNotMatchException;
import org.example.exeptions.emailToken.InvalidTokenExceptions;
import org.example.exeptions.global.FailedDoingOperation;
import org.example.exeptions.money.DontHaveEnoughMoney;
import org.example.exeptions.password.PasswordNotCorrect;
import org.example.exeptions.customer.InvalidCustomerDataException;
import org.example.exeptions.duplicate.DuplicateCommentException;
import org.example.exeptions.order.OrderPriceShouldBeHigherThanBase;
import org.example.exeptions.order.OrderStateIsNotCorrect;
import org.example.exeptions.order.StarShouldBeenBetween1To5;
import org.example.exeptions.wrongTime.TimeOfWorkDoesntMatch;
import org.example.repository.passAndUser.PassAndUserRepository;
import org.example.repository.user.BaseUserRepository;
import org.example.repository.user.customer.CustomerRepository;
import org.example.service.credit.CreditService;
import org.example.service.customerCart.CustomerCartService;
import org.example.service.emailToken.EmailTokenService;
import org.example.service.handler.HandlerService;
import org.example.service.mainService.combinedClassesService.CombinedUserClassFromCustomer;
import org.example.service.mainService.combinedClassesService.CustomerAcceptOfferClass;
import org.example.service.mapStruct.EntityMapper;
import org.example.service.offer.OfferService;
import org.example.service.order.OrderService;
import org.example.service.subHandler.SubHandlerService;
import org.example.service.user.BaseUserServiceImp;
import org.example.service.user.customer.CustomerService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImp extends BaseUserServiceImp<Customer> implements CustomerService {
    private final CustomerRepository customerRepository ;
    private final HandlerService handlerService;
    private final CustomerCartService customerCartService;
    private final CombinedUserClassFromCustomer combineUserClass;


    public CustomerServiceImp(BaseUserRepository baseUserRepository, CreditService creditService, EmailTokenService emailTokenService, EntityMapper entityMapper, CombinedUserClassFromCustomer combineUserClass, CustomerCartService customerCartService , CustomerRepository customerRepository, SubHandlerService subHandlerService, OrderService orderService, OfferService offerService, CustomerAcceptOfferClass customerAcceptOfferClass, HandlerService handlerService, PasswordEncoder passwordEncoder, PassAndUserRepository passAndUserRepository) {
        super(baseUserRepository,passwordEncoder,creditService,orderService,offerService,subHandlerService,entityMapper,emailTokenService,passAndUserRepository);
        this.customerRepository = customerRepository;
        this.handlerService = handlerService;
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
                .orElseThrow(() -> new NotFoundHandler("SubHandler not found with ID: " + subHandlerId));
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

    @Transactional
    public List<OfferDtoForCustomer> getOffersForOrder(Integer orderId)  {
             Integer customerId =(Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
             Orders orders = Optional.ofNullable(orderService.findById(orderId)).orElseThrow(()-> new NotFoundOrder("unable to find order with id : " + orderId));
             if (!Objects.equals(orders.getCustomer().getId(), customerId)) {
                 throw new NotFoundOrder();
             }
             List<Offer> offers = Optional.ofNullable(offerService.findAllOffersForSpecificOrder(orderId)).orElseThrow(() -> new NotFoundOffer("no offers found in this order with id : " + orderId));
             List<OfferDtoForCustomer> offerDtoForCustomers = new ArrayList<>();
             offers.forEach(offer -> {
                 offerDtoForCustomers.add(new OfferDtoForCustomer(offer.getId(),offer.getOfferPrice(),offer.getEmployee().getScore(),offer.getEmployee().getName() + " " + offer.getEmployee().getLast_name(),offer.getTimeOfWork(),offer.getWorkTimeInMinutes(),offer.isAccepted()));
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
    public List<SubHandlersDtoOutputId> getSubHandlersForHandler(Integer handlerId) {
        List<SubHandler> subHandlers = subHandlerService.findAllSubHandlerSameHandler(handlerId);
        List<SubHandlersDtoOutputId> subHandlersDtoOutputIds = new ArrayList<>();
        if (subHandlers!= null){
            for (SubHandler subHandler : subHandlers) {
                subHandlersDtoOutputIds.add(
                        new SubHandlersDtoOutputId(subHandler.getId(),subHandler.getName(),subHandler.getDetail(),subHandler.getBasePrice())
                );
            }
            return subHandlersDtoOutputIds;
        }
        return null;
    }

    @Transactional
    public List<OrdersOutputDtoCustomer> getAllOrders(@NotNull Integer customerId){
        List<Orders> customerOrders = orderService.findAllOrdersThatHaveSameCustomer(customerId);
        List<OrdersOutputDtoCustomer> ordersOutputDtoCustomers = new ArrayList<>();
        customerOrders.forEach(orders -> {
            ordersOutputDtoCustomers.add(new OrdersOutputDtoCustomer(orders.getId(),orders.getEmployee()!= null? orders.getEmployee().getId(): null, orders.getEmployee()!=null?orders.getEmployee().getName() + " " + orders.getEmployee().getLast_name() : null,orders.getOfferedPrice(),orders.getDetail(),Objects.isNull(orders.getSubHandler().getName()) ? "" : orders.getSubHandler().getName(),orders.getTimeOfWork(),orders.getAddress(),orders.getOrderState()));
        });
        return ordersOutputDtoCustomers;
    }

    @Transactional
    public void startOrder(Integer orderId) {
        Orders order = Optional.ofNullable(orderService.findById(orderId)).orElseThrow(() -> new NotFoundOrder());
        Integer customerId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        validateOrderForStarting(order, customerId);
        order.setOrderState(OrderState.STARTED);
        orderService.update(order);
    }

    private void validateOrderForStarting(Orders order, Integer customerId) {
        if (order.getCustomer().getId()!= customerId){
            throw new NotFoundOrder();
        }
        if (order.getOrderState() != OrderState.UNDER_REACHING_EMPLOYEE){
            throw new OrderStateIsNotCorrect();
        }
        Offer offer = Optional.ofNullable(offerService.findAcceptedOfferInOrder(order.getId())).orElseThrow(() -> new NotFoundOffer());
        if (offer.getTimeOfWork().isAfter(LocalDateTime.now())) {
            throw new TimeOfWorkDoesntMatch();
        }
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
            throw new InvalidCustomerDataException("invalid customer data for sign up");
        }
    }

    private void addCreditAndPass(Customer customer,CustomerSignUpDto customerDto){
        PassAndUser passAndUser = getPassAndUser(customerDto);
        passAndUser.setPass(passwordEncoder.encode(passAndUser.getPass()));
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

        if (checkIfNotDuplicateUser(customerDto.phone()) && validatePassWord(customerDto.password())) {
            return true;
        }
        return false;
    }

    @Override
    public CustomerLoginDtoOutput login(String user, String pass)
    {
        Customer customer = Optional.ofNullable(customerRepository.findByUser(user)).orElseThrow(NotFoundCustomer::new);
        if (passwordEncoder.matches(customer.getPassAndUser().getPass(),passwordEncoder.encode(pass))) {
        throw new PasswordNotCorrect();
    }
        return new CustomerLoginDtoOutput(customer.getId(),customer.getName(),customer.getLast_name(),customer.getPhone(),customer.getCredit().getAmount());
    }

    @Override
    public boolean checkIfNotDuplicateUser(String user) {
        return Objects.isNull(customerRepository.find(user, Customer.class));
    }
    @Override
    @Transactional
    public String addComment(Integer ordersId, Integer star, String comment){
        Orders orders = Optional.ofNullable(findOrder(ordersId)).orElseThrow(() -> new NotFoundOrder("orders not found"));
        validateOrderByCustomerId(orders);
        validateForComment(orders,star);
        orders.setComment(comment);
        orders.setScore(star);
        Integer employeeTotalScore = orders.getEmployee().getScore();
        orders.getEmployee().setScore(employeeTotalScore + star);
        orderService.update(orders);
        return "successful";
    }

    private void validateForComment(Orders orders,Integer star){
        if (orders.getScore() != null)
        {
            throw new DuplicateCommentException("score is set before");
        }
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
    public String customerChargeCart(PayToCartDto payToCartDto , String captcha ,String userCaptcha){
        if (!Objects.equals(captcha,userCaptcha)){
            throw new CaptchaDoesNotMatchException();
        }
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
            return "successful";
    }

    @Transactional
    @Override
    public String customerPay(Integer ordersId, Integer customerId){
            Orders orders = Optional.ofNullable(findOrder(ordersId)).orElseThrow(() -> new NotFoundOrder("unable to find order with id : " + ordersId));
            Customer customer = Optional.ofNullable(findCustomer(customerId)).orElseThrow(() -> new NotFoundCustomer("unable to find customer with id : " + customerId));
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
    public List<Customer> findCustomerByOptional(FindFilteredCustomerDto input) {
        return customerRepository.selectCustomerByOptional(input.name(), input.lastName(), input.email(), input.phone());
    }

    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
    public boolean validateCustomerCanPay(Orders orders, Customer customer, Offer offer ) throws DontHaveEnoughMoney {
        if (!Objects.equals(orders.getCustomer().getId(), customer.getId()))
        {
            throw new NotFoundCustomer("order does not belong to customer");
        }
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
    @Transactional
    public List<DoneDutiesDto> findDoneWorksById(Integer id) {
        List<DoneDutiesDto> doneDutiesDos = new ArrayList<>();
        List<Orders> allDoneOrders = Optional.ofNullable(orderService.findPaidOrdersForCustomer(id)).orElseThrow(()-> new NotFoundOrder("no paid order was found for this customer "));
        for (Orders orders : allDoneOrders) {
            Offer offer = offerService.findAcceptedOfferInOrder(orders.getId());
            if (offer!= null) {
                Integer employeeId = offer.getEmployee().getId();
                String employeeName = offer.getEmployee().getName() + " " + offer.getEmployee().getLast_name();
                Customer customer = customerRepository.findById(orders.getCustomer().getId(), Customer.class);
                Integer customerId = customer.getId();
                String customerFullName = customer.getName() + " " + customer.getLast_name();
                Double price = Double.valueOf(offer.getOfferPrice());
                DoneDutiesDto dto = new DoneDutiesDto(orders.getTimeOfWork(), price, new SubHandlersDtoOutput(orders.getSubHandler().getName(), orders.getSubHandler().getDetail(), orders.getSubHandler().getBasePrice()), orders.getScore() != null ? orders.getScore() : 0, employeeId, employeeName, customerFullName, customerId, orders.getComment());
                doneDutiesDos.add(dto);
            }
        }
        return doneDutiesDos;
    }

    @Override
    public List<CustomerOutputDtoForReport> findCustomerByReports(FindCustomerByFilterDto input) {
        return customerRepository.selectCustomerByReports(input.startDate(),input.endDate(),input.doneOrderStart(),input.doneOrderEnd());
    }

    @Override
    public List<OrdersOutputDtoUser> optionalSelectOrdersForCustomer(Integer customerId, String orderState) {
        List<Orders> orders = orderService.optionalFindOrdersForCustomer(customerId, orderState);
        List<OrdersOutputDtoUser> ordersOutputDtoUsers = new ArrayList<>();
        for (Orders orders1 : orders) {
            ordersOutputDtoUsers.add(new OrdersOutputDtoUser(orders1.getId(),orders1.getOfferedPrice(), orders1.getDetail(), orders1.getTimeOfWork(), orders1.getAddress(), orders1.getOrderState(), orders1.getScore(), orders1.getComment()));
        }
        return ordersOutputDtoUsers;
    }

    @Override
    public Double getCreditAmount(Integer id) {
        return Optional.ofNullable(creditService.findByCustomerId(id).getAmount()).orElseThrow(()-> new NotFoundCustomer("unable to find customer with id : "+id ));
    }

    @Override
    @Transactional
    public List<SortedOfferDtoForCustomer> sortedOfferForCustomer(SortingOffer input) {
        Orders order = Optional.ofNullable(orderService.findById(input.orderId())).orElseThrow(()-> new NotFoundOrder("unable to find order with id : "+input.orderId()));
        validateOrderByCustomerId(order);
        if (Objects.equals(order.getCustomer().getId(), input.customerId())) {
            List<Offer> offers = offerService.findAllOffersForSpecificOrder(input.orderId());
            if (input.sortByScore() && !input.ascending()) {
                List<Offer> sortedOffer =  offers.stream().sorted(Comparator.comparingInt(e -> e.getEmployee().getScore())).collect(Collectors.toList());
                return getOfferDtoForCustomers(input.sortByScore(), true, sortedOffer);
            } else if (!input.sortByScore() && !input.ascending()) {
                List<Offer> sortedOffer =  offers.stream().sorted(Comparator.comparingLong(Offer::getOfferPrice)).collect(Collectors.toList());
                return getOfferDtoForCustomers(input.sortByScore(), true, sortedOffer);
            } else if (input.sortByScore() && input.ascending()) {
                List<Offer> sortedOffer = offers.stream()
                        .sorted((e1, e2) -> Integer.compare(e2.getEmployee().getScore(), e1.getEmployee().getScore()))
                        .collect(Collectors.toList());
                return getOfferDtoForCustomers(input.sortByScore(), false, sortedOffer);
            }else {
                List<Offer> sortedOffer = offers.stream()
                        .sorted((e1, e2) -> Long.compare(e2.getOfferPrice(), e1.getOfferPrice()))
                        .collect(Collectors.toList());
                return getOfferDtoForCustomers(input.sortByScore(), false, sortedOffer);
            }
        }else {
            throw new NotFoundOrder("Unable to find order with id : "+input.orderId());
        }
    }

    private void validateOrderByCustomerId(Orders order) {
        Integer customerId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!Objects.equals(order.getCustomer().getId(), customerId)){
            throw new NotFoundOrder();
        }
    }

    private List<SortedOfferDtoForCustomer> getOfferDtoForCustomers(Boolean sortByScore, Boolean ascending, List<Offer> sortedOffer) {
        List<SortedOfferDtoForCustomer> offerDtoForCustomers = new ArrayList<>();
        for (Offer offer : sortedOffer) {
            offerDtoForCustomers.add(new SortedOfferDtoForCustomer(offer.getOfferPrice(),offer.getEmployee().getScore(),offer.getEmployee().getName() + " " + offer.getEmployee().getLast_name(),offer.getTimeOfWork(),offer.getWorkTimeInMinutes(), ascending, sortByScore));
        }
        return offerDtoForCustomers;
    }
}
