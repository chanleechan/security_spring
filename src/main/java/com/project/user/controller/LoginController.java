package com.project.user.controller;

import com.project.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<String> join(String userId, String password) {
        if (userId.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("가입하려는 정보가 유효하지 않습니다.");
        } else {
            return ResponseEntity.ok(userService.save(userId, password));
        }
    }


}
