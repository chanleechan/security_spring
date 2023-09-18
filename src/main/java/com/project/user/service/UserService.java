package com.project.user.service;

import com.project.user.domain.User;
import com.project.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository user;
    private final BCryptPasswordEncoder encoder;

    public boolean existUser(String userId) {
        return user.existsByUserId(userId);
    }

    public String save(String userId, String password) {
        if (!existUser(userId)) {
            try {
                user.save(User.create(userId, encoder.encode(password), ""));
                return "success";
            } catch (Exception e) {
                return e.getMessage();
            }

        } else {
            return "중복 회원이 있습니다.";
        }
    }


}
