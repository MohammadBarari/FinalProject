package org.example.controller.user.employee;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.password.ChangingPasswordDto;
import org.example.dto.employee.EmployeeSignUpDto;
import org.example.dto.password.changingPasswordDtoController;
import org.example.dto.employee.*;
import org.example.dto.user.LoginDto;
import org.example.dto.user.OrdersOutputDtoUser;
import org.example.enumirations.TypeOfUser;
import org.example.service.user.employee.EmployeeService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping("/signUp")
    public EmployeeSignUpDto signUp(
            @ModelAttribute @Valid
            EmployeeSignUpDto employeeSignUpDto,

            @RequestPart("image") MultipartFile image

    ) throws Exception {
        return employeeService.signUpEmployee(employeeSignUpDto);
    }

    @PostMapping("/login")
    public EmployeeLoginDtoOutput login(@RequestBody @Valid  LoginDto input) {
        return employeeService.login(input.username(), input.password());
    }

    @PostMapping("/offer")
    public OfferDto createOffer(@RequestBody @Valid OfferDtoInput offerDto) {
        Integer employeeId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OfferDto offerDto1 = new OfferDto(offerDto.offerPrice(),offerDto.timeOfWork(),offerDto.workTimeInMinutes(),offerDto.orderId(),employeeId);
        return employeeService.giveOfferToOrder(offerDto1);
    }

    @GetMapping(("/orders/all"))
    public List<OrderOutputEmployee> getAllOrders() {
        Integer employeeId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return employeeService.findOrdersForEmployee(employeeId);
    }

    @GetMapping("/subHandlers")
    public List<SubHandlerOutput> getSubHandler() {
        Integer employeeId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return employeeService.findAllSubHandlersForEmployee(employeeId);
    }

    @GetMapping("/verify")
    public String verify(@RequestParam(required = false, name = "token") String token) {
        return employeeService.validateEmployeeEmail(token);
    }

    @GetMapping("/orders")
    public List<OrdersOutputDtoUser> getOrders( @RequestParam(required = false) String orderState) {
        Integer employeeId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return employeeService.optionalSelectOrdersForEmployee(employeeId, orderState);
    }

    @GetMapping("/credit")
    public Double getCredit() {
        Integer employeeId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return employeeService.getCreditAmount(employeeId);
    }

    @PostMapping("/password/change")
    public String changePassword(@Valid @RequestBody changingPasswordDtoController changingPasswordDto) {
        return employeeService.changingPassword(new ChangingPasswordDto(changingPasswordDto.user(),
                changingPasswordDto.oldPass(), changingPasswordDto.newPass(), TypeOfUser.EMPLOYEE));
    }
}
