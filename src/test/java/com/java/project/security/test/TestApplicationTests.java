package com.java.project.security.test;

import com.project.TestApplication;
import com.project.redis.domain.RefreshToken;
import com.project.redis.domain.RefreshTokenRepository;
import com.project.security.jwt.component.JwtUtil;
import com.project.security.jwt.dto.Token;
import com.project.user.domain.AuthCode;
import com.project.user.domain.User;
import com.project.user.domain.UserAuth;
import com.project.user.domain.UserAuthPk;
import com.project.user.service.UserAuthService;
import com.project.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("test")
class TestApplicationTests {
    private final JwtUtil util;
    private final UserAuthService auth;
    private final UserService userService;

    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    TestApplicationTests(JwtUtil util, UserAuthService auth, UserService userService, RefreshTokenRepository refreshTokenRepository) {
        this.util = util;
        this.auth = auth;
        this.userService = userService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Test
    void contextLoads() {
        Token token = util.createToken("test");
        System.out.println(token);
    }

    @Test
    void getAuth() {
        AuthCode authCode = auth.findByAuthCode("01");
        User user = userService.findByUserId("test");
        UserAuth userAuth = auth.findByUserAuth(new UserAuthPk(user, authCode));
    }

    @Test
    void redisSelect() {
        RefreshToken token = refreshTokenRepository.findById("chanchan").get();
        System.out.println(token.getRefreshToken());
        System.out.println(token.getUserId());
    }
    
    @Test
    void redisSave() {
        RefreshToken token = refreshTokenRepository.save(RefreshToken.create("eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbklkIjoiY2NjIiwicm9sZXMiOiJtZW1iZXIiLCJ0eXBlIjoicmVmcmVzaCIsImlhdCI6MTY5NTY5MTg3MSwiZXhwIjoxNjk1Nzc4MjcxfQ.S0uGU8LgDP02Oh9fjuASs6oBtKgw-Blt8WMhfIuQQjJDFG1WDWw9mVlJrmdDhAjnEErv5GK1e_jSOezF2nMAgA", "ccc"));
        System.out.println(token.getRefreshToken());
        System.out.println(token.getUserId());
    }

    @Test
    void redisUpdate() {
        RefreshToken token = refreshTokenRepository.findById("chanchan").get();
        token.update("chan");
        refreshTokenRepository.save(token);
    }


}
