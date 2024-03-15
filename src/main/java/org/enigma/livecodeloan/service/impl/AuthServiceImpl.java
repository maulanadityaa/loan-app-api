package org.enigma.livecodeloan.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.enigma.livecodeloan.constant.ERole;
import org.enigma.livecodeloan.model.entity.AppUser;
import org.enigma.livecodeloan.model.entity.Customer;
import org.enigma.livecodeloan.model.entity.Role;
import org.enigma.livecodeloan.model.entity.UserCredential;
import org.enigma.livecodeloan.model.entity.UserRole;
import org.enigma.livecodeloan.model.request.AuthRequest;
import org.enigma.livecodeloan.model.request.CustomerRequest;
import org.enigma.livecodeloan.model.response.SignInResponse;
import org.enigma.livecodeloan.model.response.SignUpResponse;
import org.enigma.livecodeloan.repository.UserCredentialRepository;
import org.enigma.livecodeloan.security.JwtUtil;
import org.enigma.livecodeloan.service.AuthService;
import org.enigma.livecodeloan.service.CustomerService;
import org.enigma.livecodeloan.service.RoleService;
import org.enigma.livecodeloan.service.UserRoleService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;
    private final RoleService roleService;
    //    private final UserRoleService userRoleService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public SignUpResponse register(AuthRequest authRequest) {
        try {

            List<UserRole> userRoles = new ArrayList<>();
            for (ERole eRole : authRequest.getRoles()) {
                Role role = roleService.getOrSave(eRole);

                UserRole userRole = UserRole.builder()
                        .role(role)
                        .build();

                userRoles.add(userRole);
            }

            UserCredential userCredential = UserCredential.builder()
                    .email(authRequest.getEmail().toLowerCase())
                    .password(passwordEncoder.encode(authRequest.getPassword()))
                    .role(userRoles)
                    .build();
            userCredentialRepository.saveAndFlush(userCredential);

            for (UserRole userRole : userRoles) {
                userRole.setUser(userCredential);
            }

            CustomerRequest customerRequest = CustomerRequest.builder()
                    .firstName(authRequest.getFirstName())
                    .lastName(authRequest.getLastName())
                    .dateOfBirth(authRequest.getDob())
                    .marriageStatus(authRequest.getMarriageStatus())
                    .phone(authRequest.getMobilePhone())
                    .userCredential(userCredential)
                    .build();
            customerService.create(customerRequest);


            return SignUpResponse.builder()
                    .email(userCredential.getEmail())
                    .role(userCredential.getRole().stream().map(listRole -> listRole.getRole().getName()).toList())
                    .build();
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("User already exists");
        }
    }

    @Override
    public SignInResponse login(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getEmail().toLowerCase(),
                authRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUser appUser = (AppUser) authentication.getPrincipal();
        String token = jwtUtil.generateToken(appUser);

        return SignInResponse.builder()
                .token(token)
                .role(appUser.getRoles())
                .build();
    }
}
