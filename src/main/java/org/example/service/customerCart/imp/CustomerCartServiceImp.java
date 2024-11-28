package org.example.service.customerCart.imp;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.CustomerCart;
import org.example.exeptions.password.PasswordOrUserNameWrong;
import org.example.repository.customerCart.CustomerCartRepository;
import org.example.service.customerCart.CustomerCartService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerCartServiceImp implements CustomerCartService {
    private final CustomerCartRepository customerCartRepository;

    @Override
    @Transactional
    public CustomerCart saveCustomerCart(CustomerCart customerCart) throws PasswordOrUserNameWrong {
        if (customerCart.getCartNumber() == null && customerCart.getCvv2() == null
        ){
            throw new PasswordOrUserNameWrong();
        }
        customerCartRepository.save(customerCart);
        return customerCart;
    }

    @Override
    @Transactional
    public CustomerCart updateCustomerCart(CustomerCart customerCart) {
        customerCartRepository.save(customerCart);
        return customerCart;
    }

    @Override
    public CustomerCart getCustomerCart(Integer id) {
        return customerCartRepository.findCustomerCartById(id);
    }

    @Override
    public CustomerCart findCustomerCartByCustomerId(Integer customerId) {
        return customerCartRepository.findCustomerCartByCustomerId(customerId);
    }
}
