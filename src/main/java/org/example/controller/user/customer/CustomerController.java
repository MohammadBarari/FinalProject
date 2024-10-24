package org.example.controller.user.customer;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.domain.*;
import org.example.dto.CustomerSignUpDto;
import org.example.dto.OrderDto;
import org.example.dto.PayToCartDto;
import org.example.service.user.customer.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Customer login(@PathVariable @NotNull String username,
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
    public List<Orders> findAllOrders(@PathVariable
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
    public List<Offer> customerSeeAllOfferInOneOrder(@PathVariable @NotNull  @Digits(integer = 3,fraction = 0)
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
    public List<Handler> customerSeeAllHandlers(){
        return customerService.getHandlersForCustomer();
    }

    @GetMapping("/customerSeeAllSubHandlerForHandler/{handlerId}")
    public List<SubHandler> customerSeeAllSubHandlerForHander(@PathVariable @NotNull Integer handlerId){
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
}
