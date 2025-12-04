package org.example.controller.user.customer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.dto.captcha.CaptchaDto;
import org.example.dto.customer.*;
import org.example.dto.orders.LeavingCommentDto;
import org.example.dto.orders.OrderDto;
import org.example.dto.password.ChangingPasswordDto;
import org.example.dto.password.changingPasswordDtoController;
import org.example.dto.subHandlers.SubHandlersDtoOutputId;
import org.example.dto.user.LoginDto;
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

    @PostMapping("/signup")
    public CustomerSignUpDto signUp(@RequestBody @Valid CustomerSignUpDto dto) {
        return customerService.createCustomer(dto);
    }

    @PostMapping("/login/{username}/{password}")
    public CustomerLoginDtoOutput login(@RequestBody @Valid LoginDto input) {
        return customerService.login(input.username(), input.password());
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
    public ResponseEntity<String> customerChargeCredit(@RequestBody @Valid PayToCartDto payToCartDto) {
        captchaService.validateCaptcha(payToCartDto.captchaId(),payToCartDto.captchaAnswer());
        String response = customerService.customerChargeCart(payToCartDto);
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

    @PostMapping("/order/comment")
    public void giveComment(@RequestBody @Valid LeavingCommentDto input) {
        customerService.addComment(input.ordersId(), input.star(), input.comment());
    }

    @GetMapping("/verify")
    public String verify(@RequestParam(required = false, name = "token") String token) {
        return customerService.validateCustomerEmail(token);
    }

    @GetMapping("/orders/details")
    public List<OrdersOutputDtoUser> getOrders(@RequestParam(required = false) String orderState) {
        Integer customerId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
        Integer customerId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SortingOffer sortingOffer = new SortingOffer(customerId,input.orderId(),input.sortByScore(),input.ascending());
        return customerService.sortedOfferForCustomer(sortingOffer);
    }

    @GetMapping("/charge/success")
    public String chargeCartSuccess() {
        return "success";
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping("/captcha/generate")
    public CaptchaDto generateCaptcha() {
        return captchaService.generateCaptcha();
    }

}
