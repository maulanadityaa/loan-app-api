package org.enigma.livecodeloan.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enigma.livecodeloan.constant.DbPath;
import org.enigma.livecodeloan.constant.EApprovalStatus;
import org.enigma.livecodeloan.constant.ELoanStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Data
@Table(name = DbPath.LOAN_TRANSACTION_DETAIL_SCHEMA)
public class LoanTransactionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "transaction_date", nullable = false)
    private Long transactionDate;

    @Column(name = "nominal")
    private Double nominal;

    @Enumerated(EnumType.STRING)
    @Column(name = "loan_status")
    private ELoanStatus loanStatus;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    @ManyToOne
    @JoinColumn(name = "trx_loan_id", nullable = false)
    private LoanTransaction loanTransaction;
}
