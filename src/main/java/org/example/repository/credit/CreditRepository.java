package org.example.repository.credit;

import org.example.domain.Credit;

import java.util.List;

public interface CreditRepository {
    void save(Credit credit);
    void update(Credit credit);
    void delete(int creditId);
    Credit selectCreditById(int creditId);
    List<Credit> selectAllCredits();
    Credit selectByCustomerId(int customerId);
    Credit selectByEmployeeId(int employeeId);
    void payToEmployee(Integer customerCreditId,Integer employeeCreditId,Long offerPrice);
}
