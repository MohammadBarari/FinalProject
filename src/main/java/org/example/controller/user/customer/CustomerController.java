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

    // API 1: Sign up a new customer
    @PostMapping("/signup")
    public CustomerSignUpDto signUp(@RequestBody @Valid CustomerSignUpDto dto) {
        return customerService.createCustomer(dto);
    }

    // API 2: Customer login
    @GetMapping("/login/{username}/{password}")
    public CustomerLoginDtoOutput login(@PathVariable @NotNull String username,
                                        @PathVariable @NotNull String password) {
        return customerService.login(username, password);
    }

    // API 3: Create order for customer
    @PostMapping("/subhandler/get")
    public OrderDto getSubHandlerForCustomer(@RequestBody @Valid OrderDto dto) {
        return customerService.createOrder(dto);
    }

    // API 4: Change order to start
    @PostMapping("/order/start/{orderId}")
    public void changeOrderToStart(@PathVariable @NotNull @Digits(integer = 3, fraction = 0) Integer orderId) {
        customerService.startOrder(orderId);
    }

    // API 5: Find all orders for customer
    @GetMapping("/orders/{customerId}")
    public List<OrdersOutputDtoCustomer> findAllOrders(@PathVariable @NotNull @Digits(integer = 3, fraction = 0) Integer customerId) {
        return customerService.getAllOrders(customerId);
    }

    // API 6: Give comment on an order
    @PostMapping("/order/comment/{ordersId}/{star}/{comment}")
    public void giveComment(@PathVariable @NotNull @Digits(integer = 3, fraction = 0) Integer ordersId,
                            @PathVariable @NotNull @Digits(integer = 3, fraction = 0) Integer star,
                            @PathVariable @NotNull String comment) {
        customerService.addComment(ordersId, star, comment);
    }

    // API 7: See all offers for one order
    @GetMapping("/order/{orderId}/offers")
    public List<OfferDtoForCustomer> customerSeeAllOfferInOneOrder(@PathVariable @NotNull @Digits(integer = 3, fraction = 0) Integer orderId) {
        return customerService.getOffersForOrder(orderId);
    }

    // API 8: Accept an offer
    @PostMapping("/offer/accept/{offerId}")
    public void customerAcceptOffer(@PathVariable @NotNull @Digits(integer = 3, fraction = 0) Integer offerId) {
        customerService.customerAcceptOffer(offerId);
    }

    // API 9: See all handlers
    @GetMapping("/handlers")
    public List<HandlerCustomerDto> customerSeeAllHandlers() {
        return customerService.getHandlersForCustomer();
    }

    // API 10: See all sub-handlers for a specific handler
    @GetMapping("/handler/{handlerId}/subHandlers")
    public List<SubHandlersDtoOutputId> customerSeeAllSubHandlerForHandler(@PathVariable @NotNull Integer handlerId) {
        return customerService.getSubHandlersForHandler(handlerId);
    }

    // API 11: Charge credit
    @PostMapping("/chargeCredit")
    public ResponseEntity<String> customerChargeCredit(@RequestBody @Valid PayToCartDto payToCartDto) {
        String response = customerService.customerChargeCart(payToCartDto);
        return ResponseEntity.ok(response);
    }

    // API 12: Mark order state as done
    @PostMapping("/order/done/{orderId}")
    public String customerMakeOrderStateDone(@NotNull @PathVariable @Digits(integer = 3, fraction = 0) Integer orderId) {
        return customerService.makeServiceStateToDone(orderId);
    }

    // API 13: Pay employee for an order
    @PostMapping("/payEmployee/{orderId}/{customerId}")
    public String customerPayEmployee(@NotNull @PathVariable @Digits(integer = 3, fraction = 0) Integer orderId,
                                      @NotNull @PathVariable @Digits(integer = 3, fraction = 0) Integer customerId) {
        return customerService.customerPay(orderId, customerId);
    }

    // API 14: Employee adding comment and star rating
    @PostMapping("/order/{orderId}/commentAndStar/{star}")
    public String addCommentAndStar(@PathVariable @Digits(integer = 5, fraction = 0) Integer orderId,
                                   @RequestBody CommentDto comment,
                                    @PathVariable @Digits(integer = 5, fraction = 0) Integer star) {
        return customerService.addComment(orderId, star, comment.comment());
    }

    // API 15: Simple greeting
    @GetMapping("/hi")
    public String hi() {
        return "hi";
    }

    // API 16: Verify customer email
    @GetMapping("/verify")
    public String verify(@RequestParam(required = false, name = "token") String token) {
        return customerService.validateCustomerEmail(token);
    }

    // API 17: Get orders for a customer
    @GetMapping("/orders/{customerId}/details")
    public List<OrdersOutputDtoUser> getOrders(@PathVariable @NotNull Integer customerId, @RequestParam(required = false) String orderState) {
        return customerService.optionalSelectOrdersForCustomer(customerId, orderState);
    }

    // API 18: Get customer credit
    @GetMapping("/credit/{customerId}")
    public Double getCredit(@PathVariable @NotNull Integer customerId) {
        return customerService.getCreditAmount(customerId);
    }

    // API 19: Change password
    @PostMapping("/password/change")
    public String changePassword(@Valid @RequestBody changingPasswordDtoController changingPasswordDto) {
        return customerService.changingPassword(new ChangingPasswordDto(
                changingPasswordDto.user(),
                changingPasswordDto.oldPass(),
                changingPasswordDto.newPass(),
                TypeOfUser.CUSTOMER));
    }

    // API 20: Get sorted offers
    @GetMapping("/offers/sorted")
    public List<SortedOfferDtoForCustomer> getSortedOffer(@Valid @RequestBody SortingOfferInput input) {
        return customerService.sortedOfferForCustomer(input);
    }

    // API 21: Charge cart success
    @GetMapping("/charge/success")
    public String chargeCartSuccess() {
        return "success";
    }
}
