package com.project.user.service;

import com.project.user.domain.SecurityUser;
import com.project.user.domain.User;
import com.project.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {
    private final UserRepository userRepository;

    @Override
    public SecurityUser loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserId(userId);
        user.orElseThrow(() -> new IllegalArgumentException("UsernameNotFound"));

        SecurityUser securityUser = new SecurityUser(user.get());

        securityUser.setAccountNonExpired(true);
        securityUser.setAccountNonLocked(true);
        securityUser.setCredentialsNonExpired(true);
        securityUser.setEnabled(true);

        return securityUser;
    }
}
