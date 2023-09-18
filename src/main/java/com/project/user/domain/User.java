package com.project.user.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;

    @Column
    private String userId;

    @Column
    private String password;

    @Column
    private String userName;

    @CreatedDate
    @Column
    private LocalDateTime regDate;

    @Builder
    public User(String userId, String password, String userName) {
        this.userId = userId;
        this.password = password;
        this.userName = userName.isBlank() ? "" : userName;
        this.regDate = LocalDateTime.now();
    }

    public static User create(String userId, String password, String userName) {
        return User.builder()
                .userId(userId)
                .password(password)
                .userName(userName)
                .build();
    }
}
