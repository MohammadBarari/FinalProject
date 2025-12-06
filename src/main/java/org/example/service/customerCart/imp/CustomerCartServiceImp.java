package org.example.service.customerCart.imp;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.CustomerCart;
import org.example.repository.customerCart.CustomerCartRepository;
import org.example.service.customerCart.CustomerCartService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerCartServiceImp implements CustomerCartService {
    private final CustomerCartRepository customerCartRepository;

    @Override
    @Transactional
    public void updateCustomerCart(CustomerCart customerCart) {
        customerCartRepository.save(customerCart);
    }

    @Override
    public CustomerCart findCustomerCartByCustomerId(Integer customerId) {
        return customerCartRepository.findCustomerCartByCustomerId(customerId);
    }
}
