package org.enigma.livecodeloan.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoanDetailRequest {
    private Date transactionDate;
    private Double nominal;
    private String loanStatus;
}
