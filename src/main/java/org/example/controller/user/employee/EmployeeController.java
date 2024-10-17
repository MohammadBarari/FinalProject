package org.example.controller.user.employee;

import lombok.RequiredArgsConstructor;
import org.example.domain.Employee;
import org.example.dto.EmployeeSignUpDto;
import org.example.dto.OfferDto;
import org.example.service.user.employee.EmployeeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping("/signUp")
    public EmployeeSignUpDto signUp(@RequestBody EmployeeSignUpDto employeeSignUpDto) {
       return employeeService.signUpEmployee(employeeSignUpDto);
    }

    @GetMapping("/login/{username}/{password}")
    public Employee login(@PathVariable String username, @PathVariable String password) {
        return employeeService.login(username, password);
    }

    @GetMapping("/giveOfferToOrder")
    public OfferDto offerToOrder(@RequestBody OfferDto offerDto){
        return employeeService.GiveOfferToOrder(offerDto);
    }

}
