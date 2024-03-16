package org.enigma.livecodeloan.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enigma.livecodeloan.model.entity.Customer;
import org.enigma.livecodeloan.model.entity.InstalmentType;
import org.enigma.livecodeloan.model.entity.LoanType;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequest {
    private LoanType loanType;
    private InstalmentType instalmentType;
    private Customer customer;
    private Double nominal;
    private List<LoanDetailRequest> transactionDetailRequests;
}
