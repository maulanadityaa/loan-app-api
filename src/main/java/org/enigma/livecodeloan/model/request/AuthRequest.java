package org.enigma.livecodeloan.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String dob;
    private String address;
    private String mobilePhone;
}
