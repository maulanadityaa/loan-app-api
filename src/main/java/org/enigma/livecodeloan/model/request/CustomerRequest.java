package org.enigma.livecodeloan.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {
    private String id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String address;
    private String phone;
    private String status;
}
