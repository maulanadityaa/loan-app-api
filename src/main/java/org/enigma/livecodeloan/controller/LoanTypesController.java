package org.enigma.livecodeloan.controller;

import lombok.RequiredArgsConstructor;
import org.enigma.livecodeloan.constant.AppPath;
import org.enigma.livecodeloan.model.request.LoanTypeRequest;
import org.enigma.livecodeloan.model.response.CommonResponse;
import org.enigma.livecodeloan.model.response.LoanTypeResponse;
import org.enigma.livecodeloan.service.LoanTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(AppPath.LOAN_TYPES)
@RequiredArgsConstructor
public class LoanTypesController {
    private final LoanTypeService loanTypeService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @PostMapping
    public ResponseEntity<?> createLoanType(@RequestBody LoanTypeRequest loanTypeRequest) {
        LoanTypeResponse loanTypeResponse = loanTypeService.create(loanTypeRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<LoanTypeResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Loan type created successfully")
                        .data(loanTypeResponse)
                        .build());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @PutMapping
    public ResponseEntity<?> updateLoanType(@RequestBody LoanTypeRequest loanTypeRequest) {
        LoanTypeResponse loanTypeResponse = loanTypeService.update(loanTypeRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<LoanTypeResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Loan type updated successfully")
                        .data(loanTypeResponse)
                        .build());
    }

    @GetMapping
    public ResponseEntity<?> getAllLoanTypes() {
        List<LoanTypeResponse> loanTypeResponses = loanTypeService.getAllLoanTypes();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<List<LoanTypeResponse>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Loan types data retrieved successfully")
                        .data(loanTypeResponses)
                        .build());
    }

    @GetMapping(AppPath.GET_BY_ID)
    public ResponseEntity<?> getLoanTypeById(@PathVariable String id) {
        LoanTypeResponse loanTypeResponse = loanTypeService.getLoanTypeById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<LoanTypeResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Loan type data retrieved successfully")
                        .data(loanTypeResponse)
                        .build());
    }

    @DeleteMapping(AppPath.DELETE_BY_ID)
    public ResponseEntity<?> deleteLoanType(@PathVariable String id) {
        loanTypeService.delete(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<String>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Loan type deleted successfully")
                        .build());
    }
}
