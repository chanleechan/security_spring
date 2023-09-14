package com.project.user.service;

import com.project.user.domain.SecurityUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface SecurityService extends UserDetailsService {

    SecurityUser loadUserByUsername(String id);
}
