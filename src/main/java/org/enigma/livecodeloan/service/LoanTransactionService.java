package org.enigma.livecodeloan.service;

import org.enigma.livecodeloan.model.request.LoanApproveRequest;
import org.enigma.livecodeloan.model.request.LoanPayRequest;
import org.enigma.livecodeloan.model.request.LoanRejectRequest;
import org.enigma.livecodeloan.model.request.LoanRequest;
import org.enigma.livecodeloan.model.response.LoanResponse;

import java.util.List;

public interface LoanTransactionService {
    LoanResponse create(LoanRequest loanRequest);

    LoanResponse approve(LoanApproveRequest loanApproveRequest, String adminId);

    LoanResponse reject(LoanRejectRequest loanRejectRequest);

    List<LoanResponse> getAllLoans();

    LoanResponse getLoanById(String id);

    LoanResponse pay(String transactionId, LoanPayRequest loanPayRequest);
}
