package org.enigma.livecodeloan.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.livecodeloan.model.entity.AppUser;
import org.enigma.livecodeloan.model.entity.UserCredential;
import org.enigma.livecodeloan.model.exception.ApplicationException;
import org.enigma.livecodeloan.model.response.UserResponse;
import org.enigma.livecodeloan.repository.UserCredentialRepository;
import org.enigma.livecodeloan.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserCredentialRepository userCredentialRepository;

    @Override
    public AppUser loadUserByUserId(String userId) {
        UserCredential userCredential = userCredentialRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));

        return AppUser.builder()
                .id(userCredential.getId())
                .username(userCredential.getEmail())
                .password(userCredential.getPassword())
                .roles(userCredential.getRole().stream().map(role -> role.getRole().getName()).toList())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserCredential userCredential = userCredentialRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));

        return AppUser.builder()
                .id(userCredential.getId())
                .username(userCredential.getEmail())
                .password(userCredential.getPassword())
                .roles(userCredential.getRole().stream().map(role -> role.getRole().getName()).toList())
                .build();
    }

    @Override
    public UserResponse getUserByUserId(String userId) {
        UserCredential userCredential = userCredentialRepository.findById(userId).orElseThrow(() -> new ApplicationException("User not found", String.format("Cannot find user with id=%s", userId), HttpStatus.NOT_FOUND));

        if (userCredential != null) {
            return UserResponse.builder()
                    .email(userCredential.getEmail())
                    .role(userCredential.getRole().stream().map(role -> role.getRole().getName()).toList())
                    .build();
        }
        return null;
    }
}
