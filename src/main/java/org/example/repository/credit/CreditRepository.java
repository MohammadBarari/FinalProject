package org.example.repository.credit;

import org.example.domain.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CreditRepository extends JpaRepository<Credit, Integer> {
    Credit save(Credit credit);


    void deleteById(int creditId);


    Credit findById(int creditId);

    List<Credit> findAll();

    Credit selectByCustomerId(int customerId);
    Credit selectByEmployeeId(int employeeId);
    void payToEmployee(Integer customerCreditId,Integer employeeCreditId,Long offerPrice);
}
