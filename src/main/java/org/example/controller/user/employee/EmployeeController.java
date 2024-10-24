package org.example.controller.user.employee;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.domain.Employee;
import org.example.dto.ChangingPasswordDto;
import org.example.dto.changingPasswordDtoController;
import org.example.dto.EmployeeSignUpDto;
import org.example.dto.employee.EmployeeLoginDtoOutput;
import org.example.dto.employee.OfferDto;
import org.example.dto.employee.OrderOutputEmployee;
import org.example.dto.employee.SubHandlerOutput;
import org.example.dto.user.OrdersOutputDtoUser;
import org.example.enumirations.TypeOfUser;
import org.example.service.user.employee.EmployeeService;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping("/signUp")
    public EmployeeSignUpDto signUp(
            @RequestBody @Valid
            EmployeeSignUpDto employeeSignUpDto) throws Exception {
        return employeeService.signUpEmployee(employeeSignUpDto);
    }

    @PostMapping("/login") // Changed to POST for better practice
    public EmployeeLoginDtoOutput login(
            @RequestParam @NotNull String username, @RequestParam @NotNull String password) {
        return employeeService.login(username, password);
    }

    @PostMapping("/offer") // Shortened URL
    public OfferDto createOffer(@RequestBody @Valid OfferDto offerDto) {
        return employeeService.giveOfferToOrder(offerDto);
    }

    @GetMapping(("/orders/all/{employeeId}")) // Shortened URL
    public List<OrderOutputEmployee> getAllOrders(@PathVariable @NotNull Integer employeeId) {
        return employeeService.findOrdersForEmployee(employeeId);
    }

    @GetMapping("/subHandlers/{employeeId}") // Shortened URL
    public List<SubHandlerOutput> getSubHandler(@PathVariable @NotNull Integer employeeId) {
        return employeeService.findAllSubHandlersForEmployee(employeeId);
    }

    @GetMapping("/verify")
    public String verify(@RequestParam(required = false, name = "token") String token) {
        return employeeService.validateEmployeeEmail(token);
    }

    @GetMapping("/orders/{employeeId}") // Shortened URL
    public List<OrdersOutputDtoUser> getOrders(@PathVariable @NotNull Integer employeeId, @RequestParam(required = false) String orderState) {
        return employeeService.optionalSelectOrdersForEmployee(employeeId, orderState);
    }

    @GetMapping("/credit/{employeeId}") // Shortened URL
    public Double getCredit(@PathVariable @NotNull Integer employeeId) {
        return employeeService.getCreditAmount(employeeId);
    }

    @PostMapping("/password/change") // Shortened URL
    public String changePassword(@Valid @RequestBody changingPasswordDtoController changingPasswordDto) {
        return employeeService.changingPassword(new ChangingPasswordDto(changingPasswordDto.user(), changingPasswordDto.oldPass(), changingPasswordDto.newPass(), TypeOfUser.EMPLOYEE));
    }
}
