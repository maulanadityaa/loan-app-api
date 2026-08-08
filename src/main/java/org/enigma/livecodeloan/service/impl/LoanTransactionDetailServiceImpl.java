package org.enigma.livecodeloan.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.livecodeloan.model.entity.LoanTransactionDetail;
import org.enigma.livecodeloan.model.exception.ApplicationException;
import org.enigma.livecodeloan.repository.LoanTransactionDetailRepository;
import org.enigma.livecodeloan.service.LoanTransactionDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanTransactionDetailServiceImpl implements LoanTransactionDetailService {
    private final LoanTransactionDetailRepository loanTransactionDetailRepository;

    @Override
    public LoanTransactionDetail create(LoanTransactionDetail loanTransactionDetail) {
        return loanTransactionDetailRepository.save(loanTransactionDetail);
    }

    @Override
    public LoanTransactionDetail getById(String id) {
        return loanTransactionDetailRepository.findById(id).orElseThrow(() -> new ApplicationException("Loan Transaction Detail not found", String.format("Cannot find loan transaction detail with id=%s", id), HttpStatus.NOT_FOUND));
    }
}
