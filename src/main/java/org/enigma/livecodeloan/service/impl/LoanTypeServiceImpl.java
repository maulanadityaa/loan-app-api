package org.enigma.livecodeloan.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.livecodeloan.model.entity.LoanType;
import org.enigma.livecodeloan.model.exception.ApplicationException;
import org.enigma.livecodeloan.model.request.LoanTypeRequest;
import org.enigma.livecodeloan.model.response.LoanTypeResponse;
import org.enigma.livecodeloan.repository.LoanTypeRepository;
import org.enigma.livecodeloan.service.LoanTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanTypeServiceImpl implements LoanTypeService {
    private final LoanTypeRepository loanTypeRepository;

    @Override
    public LoanTypeResponse create(LoanTypeRequest loanTypeRequest) {
        LoanType loanType = toLoanType(loanTypeRequest);
        loanTypeRepository.save(loanType);

        return toLoanTypeResponse(loanType);
    }

    @Override
    public LoanTypeResponse update(LoanTypeRequest loanTypeRequest) {
        LoanType loanType = loanTypeRepository.findById(loanTypeRequest.getId()).orElseThrow(() -> new ApplicationException("Loan type not found", String.format("Cannot find loan type with id=%s", loanTypeRequest.getId()), HttpStatus.NOT_FOUND));

        loanType.setId(loanTypeRequest.getId());
        loanType.setType(loanTypeRequest.getLoanType());
        loanType.setMaxLoan(loanTypeRequest.getMaxLoan());
        loanTypeRepository.save(loanType);

        return toLoanTypeResponse(loanType);
    }

    @Override
    public List<LoanTypeResponse> getAllLoanTypes() {
        List<LoanType> loanTypes = loanTypeRepository.findAll();
        List<LoanTypeResponse> loanTypeResponses = new ArrayList<>();

        for (LoanType loanType : loanTypes) {
            loanTypeResponses.add(toLoanTypeResponse(loanType));
        }

        return loanTypeResponses;
    }

    @Override
    public LoanTypeResponse getLoanTypeById(String id) {
        LoanType loanType = loanTypeRepository.findById(id).orElseThrow(() -> new ApplicationException("Loan type not found", String.format("Cannot find loan type with id=%s", id), HttpStatus.NOT_FOUND));

        return toLoanTypeResponse(loanType);
    }

    @Override
    public void delete(String id) {
        LoanType loanType = loanTypeRepository.findById(id).orElseThrow(() -> new ApplicationException("Loan type not found", String.format("Cannot find loan type with id=%s", id), HttpStatus.NOT_FOUND));

        loanTypeRepository.delete(loanType);
    }

    private static LoanTypeResponse toLoanTypeResponse(LoanType loanType) {
        return LoanTypeResponse.builder()
                .id(loanType.getId())
                .loanType(loanType.getType())
                .maxLoan(loanType.getMaxLoan())
                .build();
    }

    private static LoanType toLoanType(LoanTypeRequest loanTypeRequest) {
        return LoanType.builder()
                .type(loanTypeRequest.getLoanType())
                .maxLoan(loanTypeRequest.getMaxLoan())
                .build();
    }
}
