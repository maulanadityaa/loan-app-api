package org.enigma.livecodeloan.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.enigma.livecodeloan.constant.EApprovalStatus;
import org.enigma.livecodeloan.constant.EInstalmentType;
import org.enigma.livecodeloan.constant.ELoanStatus;
import org.enigma.livecodeloan.model.entity.Customer;
import org.enigma.livecodeloan.model.entity.InstalmentType;
import org.enigma.livecodeloan.model.entity.LoanTransaction;
import org.enigma.livecodeloan.model.entity.LoanTransactionDetail;
import org.enigma.livecodeloan.model.entity.LoanType;
import org.enigma.livecodeloan.model.exception.ApplicationException;
import org.enigma.livecodeloan.model.request.LoanApproveRequest;
import org.enigma.livecodeloan.model.request.LoanPayRequest;
import org.enigma.livecodeloan.model.request.LoanRequest;
import org.enigma.livecodeloan.model.response.CustomerResponse;
import org.enigma.livecodeloan.model.response.InstalmentTypeResponse;
import org.enigma.livecodeloan.model.response.LoanDetailResponse;
import org.enigma.livecodeloan.model.response.LoanResponse;
import org.enigma.livecodeloan.model.response.LoanTypeResponse;
import org.enigma.livecodeloan.model.response.UserResponse;
import org.enigma.livecodeloan.repository.LoanTransactionRepository;
import org.enigma.livecodeloan.service.CustomerService;
import org.enigma.livecodeloan.service.InstalmentTypeService;
import org.enigma.livecodeloan.service.LoanTransactionDetailService;
import org.enigma.livecodeloan.service.LoanTransactionService;
import org.enigma.livecodeloan.service.LoanTypeService;
import org.enigma.livecodeloan.service.UserService;
import org.enigma.livecodeloan.util.Helper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanTransactionServiceImpl implements LoanTransactionService {
    private final LoanTransactionRepository loanTransactionRepository;
    private final LoanTypeService loanTypeService;
    private final InstalmentTypeService instalmentTypeService;
    private final CustomerService customerService;
    private final UserService userService;
    private final LoanTransactionDetailService loanTransactionDetailService;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public LoanResponse create(LoanRequest loanRequest) {
        try {
            LoanTypeResponse loanType = loanTypeService.getLoanTypeById(loanRequest.getLoanType().getId());
            LoanType loanTypeEntity = LoanType.builder()
                    .id(loanType.getId())
                    .type(loanType.getLoanType())
                    .maxLoan(loanType.getMaxLoan())
                    .build();

            InstalmentTypeResponse instalmentType = instalmentTypeService.getById(loanRequest.getInstalmentType().getId());
            InstalmentType instalmentTypeEntity = InstalmentType.builder()
                    .id(instalmentType.getId())
                    .instalmentType(EInstalmentType.valueOf(instalmentType.getInstalmentType()))
                    .build();

            CustomerResponse customer = customerService.getById(loanRequest.getCustomer().getId());
            Customer customerEntity = Customer.builder()
                    .id(customer.getId())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .dob(Helper.stringToDate(customer.getDateOfBirth()))
                    .marriageStatus(customer.getMarriageStatus())
                    .mobilePhone(customer.getPhone())
                    .build();

            LoanTransaction loanTransaction = new LoanTransaction();

            if (loanRequest.getNominal() <= loanType.getMaxLoan()) {
                loanTransaction = LoanTransaction.builder()
                        .loanType(loanTypeEntity)
                        .instalmentType(instalmentTypeEntity)
                        .customer(customerEntity)
                        .nominal(loanRequest.getNominal())
                        .createdAt(Instant.now().toEpochMilli())
                        .build();
                loanTransactionRepository.save(loanTransaction);
            } else {
                throw new ApplicationException("Loan nominal exceeded limit", String.format("Cannot create loan exceeded limit=%s", loanType.getMaxLoan()), HttpStatus.BAD_REQUEST);
            }

            return LoanResponse.builder()
                    .id(loanTransaction.getId())
                    .loanTypeId(loanTransaction.getLoanType().getId())
                    .instalmentTypeId(loanTransaction.getInstalmentType().getId())
                    .customerId(loanTransaction.getCustomer().getId())
                    .nominal(loanTransaction.getNominal())
                    .createdAt(loanTransaction.getCreatedAt())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("Cannot fetch customer", String.format("Cannot parse date=%s", loanRequest.getCustomer().getId()), HttpStatus.BAD_REQUEST);

        }
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public LoanResponse approve(LoanApproveRequest loanApproveRequest, String adminId) {
        LoanTransaction loanTransaction = loanTransactionRepository.findById(loanApproveRequest.getLoanTransactionId()).orElseThrow(() -> new ApplicationException("Loan Transaction not found", String.format("Cannot find loan transaction with id=%s", loanApproveRequest.getLoanTransactionId()), HttpStatus.NOT_FOUND));

        UserResponse userResponse = userService.getUserByUserId(adminId);

        loanTransaction.setApprovedAt(Instant.now().toEpochMilli());
        loanTransaction.setApprovedBy(userResponse.getEmail());
        loanTransaction.setApprovalStatus(EApprovalStatus.APPROVED);
        loanTransaction.setNominal(loanTransaction.getNominal());

        Integer counter = 0;
        switch (loanTransaction.getInstalmentType().getInstalmentType()) {
            case ONE_MONTH -> counter = 1;
            case THREE_MONTHS -> counter = 3;
            case SIXTH_MONTHS -> counter = 6;
            case NINE_MONTHS -> counter = 9;
            case TWELVE_MONTHS -> counter = 12;
        }

        List<LoanTransactionDetail> loanTransactionDetails = new ArrayList<>();

        for (int i = 0; i < counter; i++) {
            loanTransactionDetails.add(LoanTransactionDetail.builder()
                    .transactionDate(Instant.now().toEpochMilli())
                    .nominal((((loanTransaction.getNominal() * loanApproveRequest.getInterestRates()) / 100) + loanTransaction.getNominal()) / counter)
                    .loanStatus(ELoanStatus.UNPAID)
                    .createdAt(Instant.now().toEpochMilli())
                    .updatedAt(Instant.now().toEpochMilli())
                    .loanTransaction(loanTransaction)
                    .build());

            loanTransaction.setLoanTransactionDetails(loanTransactionDetails);
        }
        loanTransaction.setUpdatedAt(Instant.now().toEpochMilli());

        loanTransactionRepository.save(loanTransaction);

        List<LoanDetailResponse> loanDetailResponses = getLoanDetailResponses(loanTransaction);

        return toLoanResponse(loanTransaction, loanDetailResponses);
    }


    @Override
    public List<LoanResponse> getAllLoans() {
        List<LoanTransaction> loanTransactions = loanTransactionRepository.findAll();
        List<LoanResponse> loanResponses = new ArrayList<>();

        for (LoanTransaction loanTransaction : loanTransactions) {
            List<LoanDetailResponse> loanDetailResponses = getLoanDetailResponses(loanTransaction);

            loanResponses.add(toLoanResponse(loanTransaction, loanDetailResponses));
        }
        return loanResponses;
    }

    @Override
    public LoanResponse getLoanById(String id) {
        LoanTransaction loanTransaction = loanTransactionRepository.findById(id).orElseThrow(() -> new ApplicationException("Loan Transaction not found", String.format("Cannot find loan transaction with id=%s", id), HttpStatus.NOT_FOUND));
        List<LoanDetailResponse> loanDetailResponses = getLoanDetailResponses(loanTransaction);

        return toLoanResponse(loanTransaction, loanDetailResponses);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public LoanResponse pay(String transactionId, LoanPayRequest loanPayRequest) {
        LoanTransaction loanTransaction = loanTransactionRepository.findById(transactionId).orElseThrow(() -> new ApplicationException("Loan Transaction not found", String.format("Cannot find loan transaction with id=%s", transactionId), HttpStatus.NOT_FOUND));

        LoanTransactionDetail loanTransactionDetail = loanTransactionDetailService.getById(loanPayRequest.getLoanTransactionDetailId());
        loanTransactionDetail = LoanTransactionDetail.builder()
                .id(loanTransactionDetail.getId())
                .transactionDate(loanTransactionDetail.getTransactionDate())
                .nominal(loanTransactionDetail.getNominal())
                .loanStatus(ELoanStatus.PAID)
                .createdAt(loanTransactionDetail.getCreatedAt())
                .updatedAt(Instant.now().toEpochMilli())
                .loanTransaction(loanTransaction)
                .build();
        loanTransactionDetailService.create(loanTransactionDetail);

        List<LoanDetailResponse> loanDetailResponses = getLoanDetailResponses(loanTransaction);

        return toLoanResponse(loanTransaction, loanDetailResponses);
    }

    private static LoanResponse toLoanResponse(LoanTransaction loanTransaction, List<LoanDetailResponse> loanDetailResponses) {
        return LoanResponse.builder()
                .id(loanTransaction.getId())
                .loanTypeId(loanTransaction.getLoanType().getId())
                .instalmentTypeId(loanTransaction.getInstalmentType().getId())
                .customerId(loanTransaction.getCustomer().getId())
                .nominal(loanTransaction.getNominal())
                .approvedAt(loanTransaction.getApprovedAt())
                .approvedBy(loanTransaction.getApprovedBy())
                .approvalStatus(loanTransaction.getApprovalStatus() != null ? loanTransaction.getApprovalStatus().name() : null)
                .transactionDetailResponses(loanDetailResponses)
                .createdAt(loanTransaction.getCreatedAt())
                .updatedAt(loanTransaction.getUpdatedAt())
                .build();
    }

    private static List<LoanDetailResponse> getLoanDetailResponses(LoanTransaction loanTransaction) {
        return loanTransaction.getLoanTransactionDetails().stream().map(loanTransactionDetail -> LoanDetailResponse.builder()
                .id(loanTransactionDetail.getId())
                .transactionDate(loanTransactionDetail.getTransactionDate())
                .nominal(loanTransactionDetail.getNominal())
                .loanStatus(loanTransactionDetail.getLoanStatus().name())
                .createdAt(loanTransactionDetail.getCreatedAt())
                .updatedAt(loanTransactionDetail.getUpdatedAt())
                .build()).toList();
    }
}
