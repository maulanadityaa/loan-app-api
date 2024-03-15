package org.enigma.livecodeloan.service;

import org.enigma.livecodeloan.model.request.LoanTypeRequest;
import org.enigma.livecodeloan.model.response.LoanTypeResponse;

import java.util.List;

public interface LoanTypeService {
    LoanTypeResponse create(LoanTypeRequest loanTypeRequest);

    LoanTypeResponse update(LoanTypeRequest loanTypeRequest);

    List<LoanTypeResponse> getAllLoanTypes();

    LoanTypeResponse getLoanTypeById(String id);

    void delete(String id);
}
