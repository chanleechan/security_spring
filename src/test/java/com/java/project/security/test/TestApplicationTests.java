package com.java.project.security.test;

import com.project.TestApplication;
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

    @Autowired
    TestApplicationTests(JwtUtil util, UserAuthService auth, UserService userService) {
        this.util = util;
        this.auth = auth;
        this.userService = userService;
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


}
