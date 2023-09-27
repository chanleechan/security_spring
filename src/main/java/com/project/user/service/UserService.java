package com.project.user.service;

import com.project.security.jwt.component.JwtUtil;
import com.project.security.jwt.dto.Token;
import com.project.security.jwt.service.JwtService;
import com.project.user.domain.*;
import com.project.user.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public boolean loginExistUser(String userId, String pw) {
        if (user.findByUserId(userId).isPresent()) {
            return encoder.matches(pw, user.findByUserId(userId).get().getPassword());
        } else {
            return false;
        }
    }

    public boolean joinExistUser(String userId) {
        return user.existsByUserId(userId);
    }

    @Transactional(rollbackFor = {Exception.class})
    public ApiResponse save(String userId, String password) {
        if (!joinExistUser(userId)) {
            try {
                User u = user.save(User.create(userId, encoder.encode(password), ""));
                AuthCode code = userAuthService.findByAuthCode("01");
                userAuth.save(new UserAuth(u, code));
                Token token = jwtUtil.createToken(userId);
                //jwtService.refreshTokenSaveOrUpdate(token.getRefreshToken(), userId);
                jwtUtil.redisRefreshTokenSaveOrUpdate(token.getRefreshToken(), userId);
                return ApiResponse.create("success", token);
            } catch (Exception e) {
                throw e;
            }
        } else {
            return ApiResponse.create("fail", "중복 회원이 있습니다.");
        }
    }

    public User findByUserId(String userId) {
        return user.findByUserId(userId).orElseThrow(() -> new NoSuchElementException("UsernameNotFound"));
    }
}
