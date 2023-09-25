package com.project.user.service;

import com.project.security.jwt.component.JwtUtil;
import com.project.security.jwt.dto.Token;
import com.project.security.jwt.service.JwtService;
import com.project.user.domain.*;
import com.project.user.dto.ApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository user;
    private final UserAuthRepository userAuth;
    private final UserAuthService userAuthService;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final JwtService jwtService;

    public boolean existUser(String userId) {
        return user.existsByUserId(userId);
    }

    @Transactional
    public ApiResponse save(String userId, String password) {
        if (!existUser(userId)) {
            try {
                User u = user.save(User.create(userId, encoder.encode(password), ""));
                AuthCode code = userAuthService.findByAuthCode("01");
                userAuth.save(new UserAuth(u, code));
                Token token = jwtUtil.createToken(userId);
                jwtService.refreshTokenSaveOrUpdate(token.getRefreshToken(), userId);
                return ApiResponse.create("success", token);
            } catch (Exception e) {
                return ApiResponse.create("fail", e.getMessage());
            }
        } else {
            return ApiResponse.create("fail", "중복 회원이 있습니다.");
        }
    }

    public User findByUserId(String userId) {
        return user.findByUserId(userId).orElseThrow(() -> new NoSuchElementException("UsernameNotFound"));
    }
}
