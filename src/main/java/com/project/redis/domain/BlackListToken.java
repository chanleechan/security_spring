package com.project.redis.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "blackListToken")
@NoArgsConstructor
@Data
public class BlackListToken {
    @Id
    private String blackListToken;
    private String userId;

    @Builder
    public BlackListToken(String blackListToken, String userId) {
        this.blackListToken = blackListToken;
        this.userId = userId;
    }

    public static BlackListToken create(String blackListToken, String userId) {
        return BlackListToken.builder()
                .blackListToken(blackListToken)
                .userId(userId)
                .build();
    }
}
