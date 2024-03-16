package org.enigma.livecodeloan.controller;

import lombok.RequiredArgsConstructor;
import org.enigma.livecodeloan.constant.AppPath;
import org.enigma.livecodeloan.model.request.LoanApproveRequest;
import org.enigma.livecodeloan.model.request.LoanPayRequest;
import org.enigma.livecodeloan.model.request.LoanRejectRequest;
import org.enigma.livecodeloan.model.request.LoanRequest;
import org.enigma.livecodeloan.model.response.CommonResponse;
import org.enigma.livecodeloan.model.response.LoanResponse;
import org.enigma.livecodeloan.service.LoanTransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(AppPath.LOAN_TRANSACTIONS)
@RequiredArgsConstructor
public class LoanTransactionController {
    private final LoanTransactionService loanTransactionService;

    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER')")
    @PostMapping
    public ResponseEntity<?> createLoanTransaction(@RequestBody LoanRequest loanRequest) {
        LoanResponse loanResponse = loanTransactionService.create(loanRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<LoanResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Loan transaction created successfully")
                        .data(loanResponse)
                        .build());
    }

    @GetMapping
    public ResponseEntity<?> getAllLoanTransactions() {
        List<LoanResponse> loanResponseList = loanTransactionService.getAllLoans();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<List<LoanResponse>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Loan transactions data retrieved successfully")
                        .data(loanResponseList)
                        .build());
    }

    @GetMapping(AppPath.GET_BY_ID)
    public ResponseEntity<?> getLoanTransactionById(@PathVariable String id) {
        LoanResponse loanResponse = loanTransactionService.getLoanById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<LoanResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Loan transaction data retrieved successfully")
                        .data(loanResponse)
                        .build());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @PutMapping(AppPath.APPROVE_LOAN)
    public ResponseEntity<?> approveLoanTransaction(@PathVariable String adminId, @RequestBody LoanApproveRequest loanApproveRequest) {
        LoanResponse loanResponse = loanTransactionService.approve(loanApproveRequest, adminId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<LoanResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Loan transaction approved successfully")
                        .data(loanResponse)
                        .build());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @PutMapping(AppPath.REJECT_LOAN)
    public ResponseEntity<?> rejectLoanTransaction(@RequestBody LoanRejectRequest loanRejectRequest) {
        LoanResponse loanResponse = loanTransactionService.reject(loanRejectRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<LoanResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Loan transaction rejected")
                        .data(loanResponse)
                        .build());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @PutMapping(AppPath.PAY_LOAN)
    public ResponseEntity<?> payLoanTransaction(@PathVariable String trxId, @RequestBody LoanPayRequest loanPayRequest) {
        LoanResponse loanResponse = loanTransactionService.pay(trxId, loanPayRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<LoanResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Loan transaction paid successfully")
//                        .data(loanResponse)
                        .build());
    }
}
