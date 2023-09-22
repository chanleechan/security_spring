package com.project.user.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "jwt_refresh_token")
@NoArgsConstructor
public class JwtRefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seq;

    @Column(columnDefinition = "varchar(300)")
    private String refreshToken;

    @Column(columnDefinition = "varchar(100)")
    private String keyId;

    @CreatedDate
    @Column
    private LocalDateTime regDate;

    @Builder
    public JwtRefreshToken(String refreshToken, String keyId) {
        this.refreshToken = refreshToken;
        this.keyId = keyId;
        this.regDate = LocalDateTime.now();
    }

    public static JwtRefreshToken create(String refreshToken, String keyId) {
        return JwtRefreshToken.builder()
                .refreshToken(refreshToken)
                .keyId(keyId)
                .build();
    }

    public void update(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
