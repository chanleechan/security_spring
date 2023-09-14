package com.project.user.service;

import com.project.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository user;

    public boolean existUser(String userId, String password) {
        return user.existsByUserIdAndPassword(userId, password);
    }


}
