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
        return creditRepository.selectCreditById(id);
    }

    @Override
    public Credit findByUserId(int userId) {
        return creditRepository.selectByUserId(userId);
    }
}
