package org.example.controller.user.customer;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.domain.Customer;
import org.example.domain.Offer;
import org.example.domain.Orders;
import org.example.dto.CustomerSignUpDto;
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
    public CustomerSignUpDto signUp(@RequestBody CustomerSignUpDto dto) {
        return customerService.signUpCustomer(dto);
    }

    @GetMapping("/login/{username}/{password}")
    public Customer login(@PathVariable String username, @PathVariable String password) {
        return customerService.login(username, password);
    }

    @PostMapping("/getSubHandlerForCustomer")
    public OrderDto getSubHandlerForCustomer(@RequestBody OrderDto dto) {
        return customerService.getSubHandlerForCustomer(dto);
    }

    @PostMapping("/changeOrderToStart/{orderId}")
    public void changeOrderToStart(@PathVariable Integer orderId){
        customerService.changeOrderToStart(orderId);
    }

    @GetMapping("/findAllOrders/{customerId}")
    public List<Orders> findAllOrders(@PathVariable Integer customerId){
        return customerService.getAllOrders(customerId);
    }

    @PostMapping("/giveComment/{ordersId}/{star}/{comment}")
    public void giveComment(@PathVariable @NotNull Integer ordersId, @PathVariable Integer star,@PathVariable String comment){
        customerService.giveComment(ordersId, star, comment);
    }


    @GetMapping("/customerSeeAllOfferInOneOrder/{orderId}")
    public List<Offer> customerSeeAllOfferInOneOrder(@PathVariable  Integer orderId){
        return customerService.customerSeeAllOfferInOneOrder(orderId);
    }


    @PostMapping("/customerAcceptOffer/{offerId}")
    public void customerAcceptOffer(@PathVariable Integer offerId)
    {
        customerService.customerAcceptOffer(offerId);
    }
}
