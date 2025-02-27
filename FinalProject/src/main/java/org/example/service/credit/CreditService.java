package org.example.service.credit;

import org.example.domain.Credit;

public interface CreditService {
    void save(Credit credit);
    void update(Credit credit);
    void delete(int creditId);
    Credit findCreditById(int id);
    Credit findByCustomerId(int customerId);
    Credit findByEmployeeId(int employeeId);
    void payToEmployee(Integer customerCreditId,Integer employeeCreditId,Long offerPrice);

}
