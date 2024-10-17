package org.example.controller.user.customer;

import lombok.RequiredArgsConstructor;
import org.example.domain.Customer;
import org.example.dto.CustomerSignUpDto;
import org.example.service.user.customer.CustomerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    @PostMapping("/signUp")
    public CustomerSignUpDto signUp(@RequestBody CustomerSignUpDto dto) {
        return customerService.signUpCustomer(dto);
    }

    @GetMapping("login/{username}/{password}")
    public Customer login(@PathVariable String username, @PathVariable String password) {
        return customerService.login(username, password);
    }


}
