package org.enigma.livecodeloan.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoanDetailResponse {
    private String id;
    private Long transactionDate;
    private Double nominal;
    private String loanStatus;
    private Long createdAt;
    private Long updatedAt;
}
