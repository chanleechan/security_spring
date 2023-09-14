package com.project.user.domain;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class SecurityUser implements UserDetails {

    private static final long serivalVersionUiD = 1L;

    private Long userNo;
    private String userId;
    private String password;
    private String username;
    private LocalDateTime regDate;

    private Collection<? extends GrantedAuthority> authorities;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    public SecurityUser(User user) {
        this.userNo = user.getUserNo();
        this.userId = user.getUserId();
        this.username = user.getUserName();
        this.password = user.getPassword();
        this.regDate = user.getRegDate();
    }
}
