package com.project.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "auth_code")
public class AuthCode {
    @Id
    @Column(name = "authCode")
    private String authCode;

    @Column
    private String authName;

    @Column
    private String authCase;

    @Column
    @CreatedDate
    private LocalDateTime regDate;
}
