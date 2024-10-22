package org.example.controller.user.employee;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.domain.Employee;
import org.example.domain.Orders;
import org.example.domain.SubHandler;
import org.example.dto.EmployeeSignUpDto;
import org.example.dto.OfferDto;
import org.example.service.user.employee.EmployeeService;
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
            EmployeeSignUpDto employeeSignUpDto) {
       return employeeService.signUpEmployee(employeeSignUpDto);
    }

    @GetMapping("/login/{username}/{password}")
    public Employee login(
            @PathVariable @NotNull String username, @PathVariable @NotNull String password) {
        return employeeService.login(username, password);
    }

    @GetMapping("/giveOfferToOrder")
    public OfferDto offerToOrder(@RequestBody  @Valid OfferDto offerDto){
        return employeeService.GiveOfferToOrder(offerDto);
    }

    @GetMapping("/employeeSeeAllOrders/{employeeId}")
    public List<Orders> employeeSeeAllOrders(@PathVariable @NotNull Integer employeeId){
        return employeeService.getOrdersForEmployee(employeeId);
    }
    @GetMapping("/employeeSeeAllSubHander/{employeeId}")
    public List<SubHandler> employeeSeeAllSubHander(@PathVariable @NotNull Integer employeeId){
        return employeeService.findAllSubHandlersForEmployee(employeeId);
    }

}
