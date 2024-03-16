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
                return null;
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
            return null;
        }
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public LoanResponse approve(LoanApproveRequest loanApproveRequest, String adminId) {
        LoanTransaction loanTransaction = loanTransactionRepository.findById(loanApproveRequest.getLoanTransactionId()).orElse(null);
        if (loanTransaction != null) {
            UserResponse userResponse = userService.getUserByUserId(adminId);

            loanTransaction.setApprovedAt(Instant.now().toEpochMilli());
            loanTransaction.setApprovedBy(userResponse.getEmail());
            loanTransaction.setApprovalStatus(EApprovalStatus.APPROVED);
            loanTransaction.setNominal(((loanTransaction.getNominal() * loanApproveRequest.getInterestRates()) / 100) + loanTransaction.getNominal());

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
                        .nominal(loanTransaction.getNominal() / counter)
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

        return null;
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
        LoanTransaction loanTransaction = loanTransactionRepository.findById(id).orElse(null);
        if (loanTransaction != null) {
            List<LoanDetailResponse> loanDetailResponses = getLoanDetailResponses(loanTransaction);

            return toLoanResponse(loanTransaction, loanDetailResponses);
        }
        return null;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public LoanResponse pay(String transactionId, LoanPayRequest loanPayRequest) {
        LoanTransaction loanTransaction = loanTransactionRepository.findById(transactionId).orElse(null);

        if (loanTransaction != null) {
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
        return null;
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
