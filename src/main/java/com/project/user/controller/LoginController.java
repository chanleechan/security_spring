package com.project.user.controller;

import com.project.security.jwt.component.JwtUtil;
import com.project.security.jwt.dto.Token;
import com.project.user.dto.ApiResponse;
import com.project.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final JwtUtil token;

    @PostMapping("/user")
    public ResponseEntity<ApiResponse> userLogin(@Param("userId") String userId, @Param("pw") String pw) {

        boolean userValidation = userService.existUser(userId);
        if (userValidation) {
            Token jwtToken = token.createToken(userId);

            return ResponseEntity.ok(ApiResponse.create("success", jwtToken));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.create("fail", ""));
        }
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(String userId, String password) {
        if (userId.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("가입하려는 정보가 유효하지 않습니다.");
        } else {
            return ResponseEntity.ok(userService.save(userId, password));
        }
    }

}
