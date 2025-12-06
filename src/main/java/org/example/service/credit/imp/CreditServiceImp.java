package org.example.service.credit.imp;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.Credit;
import org.example.repository.credit.CreditRepository;
import org.example.service.credit.CreditService;
import org.springframework.stereotype.Service;

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
        creditRepository.save(credit);
    }

    @Override
    @Transactional
    public void delete(int creditId) {
        creditRepository.deleteById(creditId);
    }

    @Override
    public Credit findCreditById(int id) {
        return creditRepository.findCreditById(id);
    }

    @Override
    public Credit findByCustomerId(int customerId) {
        return creditRepository.selectByCustomerId(customerId);
    }

    @Override
    public Credit findByEmployeeId(int employeeId) {
        return creditRepository.selectByEmployeeId(employeeId);
    }

}
