package org.enigma.livecodeloan.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enigma.livecodeloan.constant.EApprovalStatus;
import org.enigma.livecodeloan.constant.EInstalmentType;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Data
@Table(name = "trx_loan")
public class LoanTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "loan_type_id")
    private LoanType loanType;

    @ManyToOne
    @JoinColumn(name = "instalment_type_id")
    private InstalmentType instalmentType;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private Double nominal;

    @Column(name = "approved_at")
    private Long approvedAt;

    @Column(name = "approved_by")
    private String approvedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status")
    private EApprovalStatus approvalStatus; // enum

    @OneToMany(mappedBy = "loanTransaction", cascade = CascadeType.PERSIST)
    private List<LoanTransactionDetail> loanTransactionDetails;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;
}
