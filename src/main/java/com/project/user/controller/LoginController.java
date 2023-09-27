package com.project.user.controller;

import com.project.security.jwt.component.JwtUtil;
import com.project.security.jwt.dto.Token;
import com.project.user.domain.User;
import com.project.user.dto.ApiResponse;
import com.project.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/user")
    public ResponseEntity<ApiResponse> userLogin(@Param("userId") String userId, @Param("pw") String pw, HttpServletRequest request) {
        boolean userValidation = userService.loginExistUser(userId, pw);
        if (userValidation) {
            Token jwtToken = jwtUtil.createToken(userId);
            ResponseCookie refreshTokenCookie = jwtUtil.setResponseCookie("refreshToken", jwtToken.getRefreshToken(), 3600L);
            ResponseCookie accessTokenCookie = jwtUtil.setResponseCookie("accessToken", jwtToken.getAccessToken(), 3600L);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                    .body(ApiResponse.create("success", jwtToken.getAccessToken()));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.create("실패", "아이디나 비밀번호가 올바르지 않습니다."));
        }
    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponse> join(User user) {
        if (user.getUserId().isEmpty() || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("가입하려는 정보가 유효하지 않습니다.");
        } else {
            try {
                return ResponseEntity.ok(userService.save(user.getUserId(), user.getPassword()));
            } catch (Exception e) {
                return ResponseEntity.ok(ApiResponse.create("fail", e.getMessage()));
            }
        }
    }

}
