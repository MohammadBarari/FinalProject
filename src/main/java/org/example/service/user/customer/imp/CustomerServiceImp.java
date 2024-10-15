package org.example.service.user.customer.imp;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.domain.Credit;
import org.example.domain.Customer;
import org.example.domain.PassAndUser;
import org.example.dto.CustomerSignUpDto;
import org.example.enumirations.TypeOfUser;
import org.example.repository.user.customer.CustomerRepository;
import org.example.service.user.BaseUserServiceImp;
import org.example.service.user.customer.CustomerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
@Service
@RequiredArgsConstructor
public class CustomerServiceImp extends BaseUserServiceImp<Customer> implements CustomerService {
    private final CustomerRepository customerRepository ;
    @SneakyThrows
    @Override
    @Transactional
    public void signUpCustomer(CustomerSignUpDto customerDto){
        if (validateCustomer(customerDto)) {
            Customer customer = new Customer();
            //settingCustomer from customerDto and pass it to base for saving
            customer.setName(customerDto.name());
            customer.setLast_name(customerDto.last_name());
            customer.setEmail(customerDto.email());
            customer.setPhone(customerDto.phone());
            customer.setTimeOfRegistration(LocalDateTime.now());
            PassAndUser passAndUser = new PassAndUser();
            passAndUser.setUsername(customerDto.phone());
            passAndUser.setPass(customerDto.password());
            Credit credit = new Credit();
            credit.setTypeOfEmployee(TypeOfUser.CUSTOMER);
            credit.setAmount(0d);
            customer.setCredit(credit);
            passAndUser.setTypeOfUser(TypeOfUser.CUSTOMER);
            savePassAndUser(passAndUser);
            //setting its value
            signUp(customer,passAndUser);
        }
    }

    @Override
    public boolean validateCustomer(CustomerSignUpDto customerDto) {
        //todo: validate everything about customer
        if (checkIfNotDuplicateUser(customerDto.phone()) && validatePassWord(customerDto.password())) {
            return true;
        }
        return false;
    }


    @Override
    public Customer login(String user, String pass)
    {
        return customerRepository.login(user,pass);
    }
    @Override
    public boolean checkIfNotDuplicateUser(String user) {
        if (Objects.isNull(customerRepository.find(user,Customer.class)))
        {
            return true;
        }
        return false;
    }
}
