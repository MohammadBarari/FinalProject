package org.example.controller.user.customer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.dto.customer.*;
import org.example.dto.subHandlers.SubHandlersDtoOutputId;
import org.example.dto.user.OrdersOutputDtoUser;
import org.example.enumirations.TypeOfUser;
import org.example.exeptions.FailedDoingOperation;
import org.example.service.user.customer.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    @PostMapping("/signUp")
    public CustomerSignUpDto signUp(@RequestBody @Valid  CustomerSignUpDto dto) {
        return customerService.createCustomer(dto);
    }

    @GetMapping("/login/{username}/{password}")
    public CustomerLoginDtoOutput login(@PathVariable @NotNull String username,
                          @PathVariable @NotNull  String password) {
        return customerService.login(username, password);
    }

    @PostMapping("/getSubHandlerForCustomer")
    public OrderDto getSubHandlerForCustomer(@RequestBody @Valid OrderDto dto) {
        return customerService.createOrder(dto);
    }

    @PostMapping("/changeOrderToStart/{orderId}")
    public void changeOrderToStart(@PathVariable @NotNull
                                       @Digits(integer = 3,fraction = 0)
                                       Integer orderId){
        customerService.startOrder(orderId);
    }

    @GetMapping("/findAllOrders/{customerId}")
    public List<OrdersOutputDtoCustomer> findAllOrders(@PathVariable
          @NotNull
          @Digits(integer = 3,fraction = 0)
                                          Integer customerId)
    {
        return customerService.getAllOrders(customerId);
    }

    @PostMapping("/giveComment/{ordersId}/{star}/{comment}")
    public void giveComment(@PathVariable @NotNull  @Digits(integer = 3,fraction = 0)Integer ordersId,
                            @PathVariable @NotNull @Digits(integer = 3,fraction = 0) Integer star,
                            @PathVariable @NotNull  String comment){
        customerService.addComment(ordersId, star, comment);
    }


    @GetMapping("/customerSeeAllOfferInOneOrder/{orderId}")
    public List<OfferDtoForCustomer> customerSeeAllOfferInOneOrder(@PathVariable @NotNull  @Digits(integer = 3,fraction = 0)
                                                         Integer orderId){
        return customerService.getOffersForOrder(orderId);
    }


    @PostMapping("/customerAcceptOffer/{offerId}")
    public void customerAcceptOffer(@PathVariable
                                        @NotNull  @Digits(integer = 3,fraction = 0)
                                        Integer offerId)
    {
        customerService.customerAcceptOffer(offerId);
    }

    @GetMapping("/customerSeeAllHandlers")
    public List<HandlerCustomerDto> customerSeeAllHandlers(){
        return customerService.getHandlersForCustomer();
    }

    @GetMapping("/customerSeeAllSubHandlerForHandler/{handlerId}")
    public List<SubHandlersDtoOutputId> customerSeeAllSubHandlerForHandler(@PathVariable @NotNull Integer handlerId){
        return customerService.getSubHandlersForHandler(handlerId);
    }

    @PostMapping("/customerChargeCredit")
    public ResponseEntity<String> customerChargeCredit(@RequestBody @Valid PayToCartDto payToCartDto){
        String response =customerService.customerChargeCart(payToCartDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/customerMakeOrderStateDone/{orderId}")
    public String customerMakeOrderStateDone(@NotNull @PathVariable @Digits(integer = 3,fraction = 0) Integer orderId
    ){
        return customerService.makeServiceStateToDone(orderId);
    }
    @PostMapping("/customerPayEmployee/{orderId}/{customerId}")
    public String customerPayEmployee(@NotNull @PathVariable @Digits(integer = 3,fraction = 0) Integer orderId
            ,@NotNull @PathVariable @Digits(integer = 3,fraction = 0) Integer customerId){
        return customerService.customerPay(orderId, customerId);
    }

    @PostMapping("/employeeAddingCommentAndStar/{orderId}/{star}")
    public String addCommentAndStar( @PathVariable @Digits(integer = 1,fraction = 0) Integer orderId,
            @RequestParam(required = false) String comment ,
                                    @PathVariable @Digits(integer = 1,fraction = 0)  Integer star){
        return customerService.addComment(orderId, star, comment);
    }

    @GetMapping("/hi")
    public String hi(){
        return "hi";
    }

    @GetMapping("/verify")
    public String verify(@RequestParam(required = false ,name = "token") String token){
        return customerService.validateCustomerEmail(token);
    }
    @GetMapping("/getOrders/{customerId}")
    public List<OrdersOutputDtoUser> getOrders(@PathVariable @NotNull Integer customerId , @RequestParam(required = false) String orderState){
        if (Objects.isNull(customerId)){
            throw new FailedDoingOperation("customerId is null");
        }
        return customerService.optionalSelectOrdersForCustomer(customerId,orderState);

    }
    @GetMapping("/creit/getCredit/{customerId}")
    public Double getCredit(@PathVariable  @NotNull  Integer customerId ){
        return customerService.getCreditAmount(customerId);
    }
    @PostMapping("/passWord/change")
    public String changePassword(@Valid @RequestBody changingPasswordDtoController changingPasswordDto){
        return customerService.changingPassword(new ChangingPasswordDto(changingPasswordDto.user(),changingPasswordDto.oldPass(),changingPasswordDto.newPass(), TypeOfUser.CUSTOMER));
    }
    @GetMapping("/offer/getSortedOffer")
    public List<SortedOfferDtoForCustomer> getSortedOffer(@Valid @RequestBody SortingOfferInput input){
        return customerService.sortedOfferForCustomer(input);
    }
    @GetMapping("/charge_cart/success")
    public String chargeCartSuccess(){
        return "success";
    }
}
