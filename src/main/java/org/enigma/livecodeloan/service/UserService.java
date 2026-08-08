package org.enigma.livecodeloan.service;

import org.enigma.livecodeloan.model.entity.AppUser;
import org.enigma.livecodeloan.model.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    AppUser loadUserByUserId(String userId);

    UserResponse getUserByUserId(String userId);
}
