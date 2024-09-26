package org.example.service.credit;

import org.example.domain.Credit;

public interface CreditService {
    void save(Credit credit);
    void update(Credit credit);
    void delete(int creditId);
    Credit findCreditById(int id);
    Credit findByUserId(int userId);
    Credit findByCus
}
