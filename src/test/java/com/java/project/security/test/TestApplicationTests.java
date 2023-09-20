package com.java.project.security.test;

import com.project.TestApplication;
import com.project.security.jwt.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("test")
class TestApplicationTests {
    private final JwtUtil util;

    @Autowired
    TestApplicationTests(JwtUtil util) {
        this.util = util;
    }

    @Test
    void contextLoads() {
        String token = util.createToken("test");
        System.out.println(token);
    }


}
