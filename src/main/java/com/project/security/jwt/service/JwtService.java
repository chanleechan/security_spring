package com.project.security.jwt.service;

import com.project.security.jwt.component.JwtUtil;
import com.project.user.domain.JwtRefreshToken;
import com.project.user.domain.JwtRefreshTokenRepository;
import com.project.user.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    private final JwtUtil util;
    private final JwtRefreshTokenRepository jwtRefreshTokenRepository;

    public ApiResponse validateRefreshToken(String refreshToken) {
        JwtRefreshToken refresh = Optional.ofNullable(jwtRefreshTokenRepository.findByRefreshToken(refreshToken))
                .orElseThrow(() -> new NoSuchElementException("no search refresh Token : "));
        String createAccessToken = util.validateRefreshToken(refresh);
        return createRefreshJson(createAccessToken);
    }

    public ApiResponse createRefreshJson(String accessToken) {
        Map<String, String> map = new HashMap<>();

        if (accessToken.isEmpty()) {
            map.put("errorType", "Forbidden");
            map.put("status", "402");
            map.put("resultMsg", "Refresh 토큰 만료. 로그인 필요");

        } else {
            map.put("status", "200");
            map.put("resultMsg", "Refresh 토큰을 통한 AccessToken 생성 완료");
            map.put("accessToken", accessToken);

        }
        return ApiResponse.create("fail", map);
    }

    public void refreshTokenSaveOrUpdate(String refreshToken, String loginId) {
        jwtRefreshTokenRepository.findByKeyId(loginId)
                .ifPresentOrElse(
                        jwtRefreshToken -> {
                            jwtRefreshToken.update(refreshToken);
                            jwtRefreshTokenRepository.save(jwtRefreshToken);
                        },
                        () -> {
                            jwtRefreshTokenRepository.save(JwtRefreshToken.create(refreshToken, loginId));
                        }
                );
    }
}
