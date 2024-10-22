package org.example.service.credit.imp;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.Credit;
import org.example.exeptions.NotFoundUser;
import org.example.repository.credit.CreditRepository;
import org.example.service.credit.CreditService;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreditServiceImp implements CreditService {

    private final CreditRepository creditRepository;
    @Override
    @Transactional
    public void save(Credit credit) {
        creditRepository.save(credit);
    }

    @Override
    @Transactional
    public void update(Credit credit) {
        creditRepository.update(credit);
    }

    @Override
    @Transactional
    public void delete(int creditId) {
        creditRepository.delete(creditId);
    }

    @Override
    public Credit findCreditById(int id) {
        return Optional.ofNullable(creditRepository.selectCreditById(id)).orElseThrow(()-> new NotFoundUser("Unable to find credit with id " + id));
    }

    @Override
    public Credit findByCustomerId(int customerId) {
        return Optional.ofNullable(creditRepository.selectByCustomerId(customerId)).orElseThrow(()-> new NotFoundUser("Unable to find customer with id " + customerId));
    }

    @Override
    public Credit findByEmployeeId(int employeeId) {
        return Optional.ofNullable(creditRepository.selectByEmployeeId(employeeId)).orElseThrow(()-> new NotFoundUser("Unable to find credit with employeeId " + employeeId));
    }

    @Override
    @Transactional
    public void payToEmployee(Integer customerCreditId, Integer employeeCreditId, Long offerPrice) {
        creditRepository.payToEmployee(customerCreditId,employeeCreditId,offerPrice);
    }

}
