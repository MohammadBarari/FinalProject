package org.example.service.credit.imp;

import org.example.domain.Credit;
import org.example.repository.credit.CreditRepository;
import org.example.repository.credit.imp.CreditRepositoryImp;
import org.example.service.credit.CreditService;

public class CreditServiceImp implements CreditService {

    CreditRepository creditRepository = new CreditRepositoryImp();
    @Override
    public void save(Credit credit) {
        creditRepository.save(credit);
    }

    @Override
    public void update(Credit credit) {
        creditRepository.update(credit);
    }

    @Override
    public void delete(int creditId) {
        creditRepository.delete(creditId);
    }

    @Override
    public Credit findCreditById(int id) {
        return creditRepository.findById(id);
    }

    @Override
    public Credit findByCustomerId(int customerId) {
        return creditRepository.selectByCustomerId(customerId);
    }

    @Override
    public Credit findByEmployeeId(int employeeId) {
        return creditRepository.selectByEmployeeId(employeeId);
    }

    @Override
    public void payToEmployee(Integer customerCreditId, Integer employeeCreditId, Long offerPrice) {
        creditRepository.payToEmployee(customerCreditId,employeeCreditId,offerPrice);
    }

}
