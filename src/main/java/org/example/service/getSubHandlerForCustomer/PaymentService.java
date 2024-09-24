package org.example.service.getSubHandlerForCustomer;

import org.example.domain.Credit;
import org.example.domain.Customer;
import org.example.service.credit.CreditService;
import org.example.service.credit.imp.CreditServiceImp;

import javax.smartcardio.CardTerminal;

public class PaymentService {
    private CreditService creditService;

    public PaymentService() {
        creditService = new CreditServiceImp();
    }
    public void customerChargeCart(Customer customer, double amount){
        try {
            Credit credit = creditService.findById(customer.getCredit().getId())
        }

    }
}

