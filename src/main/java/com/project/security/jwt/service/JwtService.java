package com.project.security.jwt.service;

import com.project.redis.domain.RefreshToken;
import com.project.redis.domain.RefreshTokenRepository;
import com.project.security.jwt.component.JwtUtil;
import com.project.user.domain.JwtRefreshToken;
import com.project.user.domain.JwtRefreshTokenRepository;
import com.project.user.dto.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    private final JwtUtil util;
    private final JwtRefreshTokenRepository jwtRefreshTokenRepository;
    private final RefreshTokenRepository redisRefreshTokenRepository;

    public ApiResponse validateRefreshToken(String refreshToken) {
        RefreshToken redisRefreshToken = redisRefreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new NoSuchElementException("no search refresh Token : "));
        String createAccessToken = util.validateRedisRefreshToken(redisRefreshToken);
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
        return ApiResponse.create("success", map);
    }

    //refresh 토큰 재 생성 or db 업데이트
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

    public Cookie getTokenCookie(HttpServletRequest request, String cookieName) {
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(cookieName))
                .findFirst()
                .orElse(null);
    }

    public String getToken(Cookie cookie) {
        if (cookie == null) {
            return "";
        } else {
            return cookie.getValue();
        }
    }
}
