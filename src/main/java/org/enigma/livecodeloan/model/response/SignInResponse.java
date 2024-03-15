package org.enigma.livecodeloan.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.enigma.livecodeloan.constant.ERole;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponse {
    private String token;
    private List<ERole> role;
}
