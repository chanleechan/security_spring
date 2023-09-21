package com.project.user.service;

import com.project.user.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {
    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;
    private final UserAuthService userAuthService;

    @Override
    public SecurityUser loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserId(userId);
        user.orElseThrow(() -> new IllegalArgumentException("UsernameNotFound"));

        SecurityUser securityUser = new SecurityUser(user.get());
        AuthCode authCode = userAuthService.findByAuthCode("01");
        securityUser.setAuthorities(
                userAuthRepository.findById(new UserAuthPk(user.get(), authCode))
                        .stream()
                        .map(o -> new SimpleGrantedAuthority(o.getAuthName()))
                        .collect(Collectors.toList())
        );

        securityUser.setAccountNonExpired(true);
        securityUser.setAccountNonLocked(true);
        securityUser.setCredentialsNonExpired(true);
        securityUser.setEnabled(true);

        return securityUser;
    }
}
