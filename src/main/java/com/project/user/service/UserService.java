package com.project.user.service;

import com.project.user.domain.*;
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

    public boolean existUser(String userId) {
        return user.existsByUserId(userId);
    }

    public String save(String userId, String password) {
        if (!existUser(userId)) {
            try {
                User u = user.save(User.create(userId, encoder.encode(password), ""));
                AuthCode code = userAuthService.findByAuthCode("01");
                userAuth.save(new UserAuth(u, code));
                return "success";
            } catch (Exception e) {
                return e.getMessage();
            }

        } else {
            return "중복 회원이 있습니다.";
        }
    }

    public User findByUserId(String userId) {
        return user.findByUserId(userId).orElseThrow(() -> new NoSuchElementException("UsernameNotFound"));
    }
}
