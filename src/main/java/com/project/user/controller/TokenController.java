package com.project.user.controller;

import com.project.security.jwt.service.JwtService;
import com.project.user.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class TokenController {
    private final JwtService service;

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> validateRefreshToken(HttpServletRequest request) {
        String refreshToken = service.getToken(service.getTokenCookie(request, "refreshToken"));
        /*String refreshToken = request.getHeader("refreshToken");*/
        try {
            return ResponseEntity.ok(service.validateRefreshToken(refreshToken));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>("fail", e.getMessage()));
        }
    }

 /*   @GetMapping("/test")
    public ResponseEntity<String> test(HttpServletRequest request, HttpServletResponse response) {
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", "access").build();
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "refresh").build();
        response.addHeader("Set-Cookie", accessCookie + ";httpOnly");
        response.addHeader("Set-Cookie", refreshCookie + ";httpOnly");
        return ResponseEntity.ok("success");
    }*/
}
