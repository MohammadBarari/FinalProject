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
import org.example.service.captcha.CaptchaService;
import org.example.service.user.customer.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final CaptchaService captchaService;
    private String captcha;

    @PostMapping("/signup")
    public CustomerSignUpDto signUp(@RequestBody @Valid CustomerSignUpDto dto) {
        return customerService.createCustomer(dto);
    }


    @GetMapping("/login/{username}/{password}")
    public CustomerLoginDtoOutput login(@PathVariable @NotNull String username,
                                        @PathVariable @NotNull String password) {
        return customerService.login(username, password);
    }


    @PostMapping("/subhandler/get")
    public OrderDto getSubHandlerForCustomer(@RequestBody @Valid OrderDtoInput dto) {
        Integer customerId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OrderDto orderDto =new OrderDto(dto.offeredPrice(),dto.detail(),dto.timeOfWork(),dto.address(),dto.subHandlerId(),customerId);
        return customerService.createOrder(orderDto);
    }


    @PostMapping("/order/start/{orderId}")
    public void changeOrderToStart(@PathVariable @NotNull @Digits(integer = 5, fraction = 0) Integer orderId) {
        customerService.startOrder(orderId);
    }


    @GetMapping("/orders")
    public List<OrdersOutputDtoCustomer> findAllOrders() {
        Integer customerId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(customerId);
        return customerService.getAllOrders(customerId);
    }


    @PostMapping("/order/comment/{ordersId}/{star}/{comment}")
    public void giveComment(@PathVariable @NotNull @Digits(integer = 5, fraction = 0) Integer ordersId,
                            @PathVariable @NotNull @Digits(integer = 5, fraction = 0) Integer star,
                            @PathVariable @NotNull String comment) {
        customerService.addComment(ordersId, star, comment);
    }


    @GetMapping("/order/{orderId}/offers")
    public List<OfferDtoForCustomer> customerSeeAllOfferInOneOrder(@PathVariable @NotNull @Digits(integer = 5, fraction = 0) Integer orderId) {
        return customerService.getOffersForOrder(orderId);
    }


    @PostMapping("/offer/accept/{offerId}")
    public void customerAcceptOffer(@PathVariable @NotNull @Digits(integer = 5, fraction = 0) Integer offerId) {
        customerService.customerAcceptOffer(offerId);
    }

    @GetMapping("/handlers")
    public List<HandlerCustomerDto> customerSeeAllHandlers() {
        return customerService.getHandlersForCustomer();
    }


    @GetMapping("/handler/{handlerId}/subHandlers")
    public List<SubHandlersDtoOutputId> customerSeeAllSubHandlerForHandler(@PathVariable @NotNull Integer handlerId) {
        return customerService.getSubHandlersForHandler(handlerId);
    }

    @PostMapping("/chargeCredit")
    public ResponseEntity<String> customerChargeCredit(@RequestBody @Valid PayToCartDto payToCartDto , @RequestParam String captcha) {
        System.out.println(this.captcha + " : " +captcha);
        String response = customerService.customerChargeCart(payToCartDto,this.captcha,captcha);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/order/done/{orderId}")
    public String customerMakeOrderStateDone(@NotNull @PathVariable @Digits(integer = 5, fraction = 0) Integer orderId) {
        return customerService.makeServiceStateToDone(orderId);
    }


    @PostMapping("/payEmployee/{orderId}")
    public String customerPayEmployee(@NotNull @PathVariable @Digits(integer = 5, fraction = 0) Integer orderId) {
        Integer customerId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        return customerService.customerPay(orderId, customerId);
    }


    @PostMapping("/order/{orderId}/commentAndStar/{star}")
    public String addCommentAndStar(@PathVariable @Digits(integer = 5, fraction = 0) Integer orderId,
                                    @RequestBody CommentDto comment,
                                    @PathVariable @Digits(integer = 5, fraction = 0) Integer star) {
        return customerService.addComment(orderId, star, comment.comment());
    }


    @GetMapping("/hi")
    public String hi() {
        return "hi";
    }


    @GetMapping("/verify")
    public String verify(@RequestParam(required = false, name = "token") String token) {
        return customerService.validateCustomerEmail(token);
    }


    @GetMapping("/orders/{customerId}/details")
    public List<OrdersOutputDtoUser> getOrders(@PathVariable @NotNull Integer customerId, @RequestParam(required = false) String orderState) {
        return customerService.optionalSelectOrdersForCustomer(customerId, orderState);
    }


    @GetMapping("/credit")
    public Double getCredit() {
        Integer customerId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return customerService.getCreditAmount(customerId);
    }


    @PostMapping("/password/change")
    public String changePassword(@Valid @RequestBody changingPasswordDtoController changingPasswordDto) {
        return customerService.changingPassword(new ChangingPasswordDto(
                changingPasswordDto.user(),
                changingPasswordDto.oldPass(),
                changingPasswordDto.newPass(),
                TypeOfUser.CUSTOMER));
    }


    @GetMapping("/offers/sorted")
    public List<SortedOfferDtoForCustomer> getSortedOffer(@Valid @RequestBody SortingOfferInput input) {
        return customerService.sortedOfferForCustomer(input);
    }


    @GetMapping("/charge/success")
    public String chargeCartSuccess() {
        return "success";
    }
    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping("/captcha/generate")
    public String generateCaptcha() {
        captcha = captchaService.generateCaptcha();
        System.out.println(captcha);
        return captcha;
    }

}
