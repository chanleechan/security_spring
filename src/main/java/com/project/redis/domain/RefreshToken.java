package com.project.redis.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken", timeToLive = 2592000)
@NoArgsConstructor
@Data
public class RefreshToken {
    @Id
    private String refreshToken;
    private String userId;

    @Builder
    public RefreshToken(String refreshToken, String userId) {
        this.refreshToken = refreshToken;
        this.userId = userId;
    }

    public static RefreshToken create(String refreshToken, String userId) {
        return RefreshToken.builder()
                .refreshToken(refreshToken)
                .userId(userId)
                .build();
    }

    public void update(String refreshToken) {
        this.refreshToken = refreshToken;
    }


}
