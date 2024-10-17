package org.example.controller.user.customer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.domain.*;
import org.example.dto.CustomerSignUpDto;
import org.example.dto.HandlerDto;
import org.example.dto.OrderDto;
import org.example.service.user.customer.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    @PostMapping("/signUp")
    public CustomerSignUpDto signUp(@RequestBody @Valid  CustomerSignUpDto dto) {
        return customerService.signUpCustomer(dto);
    }

    @GetMapping("/login/{username}/{password}")
    public Customer login(@PathVariable @NotNull String username,
                          @PathVariable @NotNull  String password) {
        return customerService.login(username, password);
    }

    @PostMapping("/getSubHandlerForCustomer")
    public OrderDto getSubHandlerForCustomer(@RequestBody @Valid OrderDto dto) {
        return customerService.getSubHandlerForCustomer(dto);
    }

    @PostMapping("/changeOrderToStart/{orderId}")
    public void changeOrderToStart(@PathVariable @NotNull
                                       @Digits(integer = 3,fraction = 0)
                                       Integer orderId){
        customerService.changeOrderToStart(orderId);
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
        customerService.giveComment(ordersId, star, comment);
    }


    @GetMapping("/customerSeeAllOfferInOneOrder/{orderId}")
    public List<Offer> customerSeeAllOfferInOneOrder(@PathVariable @NotNull  @Digits(integer = 3,fraction = 0)
                                                         Integer orderId){
        return customerService.customerSeeAllOfferInOneOrder(orderId);
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
        return customerService.customerSeeAllHandlers();
    }

    @GetMapping("/customerSeeAllSubHandlerForHander/{handlerId}")
    public List<SubHandler> customerSeeAllSubHandlerForHander(@NotNull Integer handlerId){
        return customerService.findAllSubHandlerForHandler(handlerId);
    }
}
