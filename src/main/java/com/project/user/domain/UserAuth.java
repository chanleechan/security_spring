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
@IdClass(UserAuthPk.class)
@Table(name = "user_auth")
public class UserAuth {
    @Id
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User userId;

    @Id
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "auth_code", referencedColumnName = "authCode")
    private AuthCode authCode;

    @Column
    private String authName;

    @Column
    @CreatedDate
    private LocalDateTime regDate;

    @Builder
    public UserAuth(User userId, AuthCode authCode) {
        this.userId = userId;
        this.authCode = authCode;
        this.authName = authCode.getAuthName();
    }

}
