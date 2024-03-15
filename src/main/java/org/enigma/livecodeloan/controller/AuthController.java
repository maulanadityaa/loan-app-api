package org.enigma.livecodeloan.controller;

import lombok.RequiredArgsConstructor;
import org.enigma.livecodeloan.model.request.AuthRequest;
import org.enigma.livecodeloan.model.response.CommonResponse;
import org.enigma.livecodeloan.model.response.SignInResponse;
import org.enigma.livecodeloan.model.response.SignUpResponse;
import org.enigma.livecodeloan.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUpAdminOrStaff(@RequestBody AuthRequest authRequest) {
        SignUpResponse signUpResponse = authService.register(authRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<SignUpResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("User signed up successfully")
                        .data(signUpResponse)
                        .build());
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody AuthRequest authRequest) {
        SignInResponse signInResponse = authService.login(authRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<SignInResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Login successful")
                        .data(signInResponse)
                        .build());

    }
}
