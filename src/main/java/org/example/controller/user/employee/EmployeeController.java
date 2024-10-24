package org.example.controller.user.employee;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.domain.Employee;
import org.example.dto.EmployeeSignUpDto;
import org.example.dto.employee.OfferDto;
import org.example.dto.employee.OrderOutputEmployee;
import org.example.dto.employee.SubHandlerOutput;
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
            EmployeeSignUpDto employeeSignUpDto) throws Exception {
       return employeeService.signUpEmployee(employeeSignUpDto);
    }

    @GetMapping("/login/{username}/{password}")
    public Employee login(
            @PathVariable @NotNull String username, @PathVariable @NotNull String password) {
        return employeeService.login(username, password);
    }

    @GetMapping("/giveOfferToOrder")
    public OfferDto offerToOrder(@RequestBody  @Valid OfferDto offerDto){
        return employeeService.giveOfferToOrder(offerDto);
    }

    @GetMapping("/employeeSeeAllOrders/{employeeId}")
    public List<OrderOutputEmployee> employeeSeeAllOrders(@PathVariable @NotNull Integer employeeId){
         return employeeService.getOrdersForEmployee(employeeId);
    }
    @GetMapping("/employeeSeeAllSubHander/{employeeId}")
    public List<SubHandlerOutput> employeeSeeAllSubHander(@PathVariable @NotNull Integer employeeId){
        return employeeService.findAllSubHandlersForEmployee(employeeId);
    }
    @GetMapping("/verify")
    public String verify(@RequestParam(required = false ,name = "token") String token){
        return employeeService.validateEmployeeEmail(token);
    }
}
