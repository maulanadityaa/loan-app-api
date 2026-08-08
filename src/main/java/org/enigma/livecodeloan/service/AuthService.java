package org.enigma.livecodeloan.service;

import org.enigma.livecodeloan.model.request.AuthRequest;
import org.enigma.livecodeloan.model.response.SignInResponse;
import org.enigma.livecodeloan.model.response.SignUpResponse;

public interface AuthService {
    SignUpResponse register(AuthRequest authRequest);

//    SignUpResponse registerAdminOrStaff(AuthRequest authRequest);

    SignInResponse login(AuthRequest authRequest);
}
