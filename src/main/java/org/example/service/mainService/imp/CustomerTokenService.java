package org.example.service.mainService.imp;

import org.example.domain.Customer;
import org.example.domain.EmailToken;
import org.example.exeptions.InvalidTokenExceptions;
import org.example.service.emailToken.EmailTokenService;
import org.example.service.user.customer.CustomerService;
import org.example.service.user.employee.EmployeeService;
import org.hibernate.annotations.ColumnTransformers;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomerTokenService {
    private final EmailTokenService tokenService;
    private final CustomerService customerService;
    private final EmployeeService employeeService;
    public CustomerTokenService(EmailTokenService tokenService,EmployeeService employeeService ,CustomerService customerService) {
        this.tokenService = tokenService;
        this.customerService = customerService;
        this.employeeService = employeeService;
    }

    public void validateUser(EmailToken emailToken) {
        switch (emailToken.getTypeOfUser()){
            case CUSTOMER ->{
                validateCustomerToken(emailToken);
            }
            case EMPLOYEE ->{
                validateEmployeeToken(emailToken);
            }
        }
    }
    public void validateCustomerToken(EmailToken emailToken) {
        if (employeeService.employeeExistsByEmail(emailToken.getEmail())){
            employeeService.setUnderReviewState(emailToken.getEmail());
            emailToken.setExpired(true);
        }

    }

    public void validateEmployeeToken(EmailToken emailToken) {
       Customer customer = Optional.ofNullable(customerService.findByEmail(emailToken.getEmail())).orElseThrow(() -> new InvalidTokenExceptions("The token is not from the user"));
       customer.setActive(true);
       emailToken.setExpired(true);
    }
}
