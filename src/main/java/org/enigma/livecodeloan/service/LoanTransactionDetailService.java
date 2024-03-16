package org.enigma.livecodeloan.service;

import org.enigma.livecodeloan.model.entity.LoanTransactionDetail;

public interface LoanTransactionDetailService {
    LoanTransactionDetail create(LoanTransactionDetail loanTransactionDetail);

    LoanTransactionDetail getById(String id);
}
