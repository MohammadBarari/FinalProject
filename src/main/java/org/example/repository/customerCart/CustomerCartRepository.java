package org.example.repository.customerCart;

import org.example.domain.CustomerCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerCartRepository extends JpaRepository<CustomerCart, Integer> {
    CustomerCart findCustomerCartById(Integer id);

    @Query(value = """
      select * from customtestschema.customer_cart where customer_id = ?1
""",nativeQuery = true)
    CustomerCart findCustomerCartByCustomerId(Integer customerId);
}
