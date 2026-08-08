package org.enigma.livecodeloan.controller;

import lombok.RequiredArgsConstructor;
import org.enigma.livecodeloan.constant.AppPath;
import org.enigma.livecodeloan.model.response.CommonResponse;
import org.enigma.livecodeloan.model.response.UserResponse;
import org.enigma.livecodeloan.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppPath.USERS)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(AppPath.GET_BY_ID)
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        UserResponse userResponse = userService.getUserByUserId(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<UserResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("User found")
                        .data(userResponse)
                        .build());
    }
}
