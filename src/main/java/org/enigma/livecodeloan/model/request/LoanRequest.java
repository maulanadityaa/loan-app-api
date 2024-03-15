package org.enigma.livecodeloan.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequest {
    private String loanTypeId;
    private String instalmentTypeId;
    private String customerId;
    private Long nominal;
}
