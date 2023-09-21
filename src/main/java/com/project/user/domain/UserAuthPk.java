package com.project.user.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAuthPk implements Serializable {
    private User userId;
    private AuthCode authCode;

    public UserAuthPk(User user, AuthCode authCode) {
        this.userId = user;
        this.authCode = authCode;
    }
}
