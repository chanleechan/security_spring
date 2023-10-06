package com.project.security.oauth.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "social_user")
@Builder
public class SocialUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;

    @Column
    private String userName;

    @Column
    private String email;

    @Column
    private String provider;

    @CreatedDate
    @Column
    private LocalDateTime regDate;

    public static SocialUser create(String userName, String email, String provider) {
        return SocialUser.builder()
                .userName(userName)
                .email(email)
                .provider(provider)
                .regDate(LocalDateTime.now())
                .build();
    }

}
