package com.project.user.service;

import com.project.user.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserAuthRepository userAuthRepository;
    private final AuthCodeRepository authCodeRepository;

    public UserAuth findByUserAuth(UserAuthPk userAuthPk) {
        return userAuthRepository.findById(userAuthPk).orElseThrow(() -> new IllegalArgumentException("not found UserAuth"));
    }

    public AuthCode findByAuthCode(String authCode) {
        return authCodeRepository.findById(authCode).orElseThrow(() -> new IllegalArgumentException("now found AuthCode"));
    }
}
