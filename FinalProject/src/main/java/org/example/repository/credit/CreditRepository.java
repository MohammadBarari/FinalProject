package org.example.repository.credit;

import org.example.domain.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CreditRepository extends JpaRepository<Credit, Integer> {

    Credit findCreditById(int creditId);


    @Query(value = """
                select * from credit
            where id = (select c.id from customer join credit c on c.id = customer.credit_id
        where customer.id = ?1)
""",nativeQuery = true)
    Credit selectByCustomerId(int customerId);

    @Query(value = """
                select * from credit
            where id = (select c.id from customer join credit c on c.id = customer.credit_id
        where customer.id = ?1)
""",nativeQuery = true)
    Credit selectByEmployeeId(int employeeId);

}
