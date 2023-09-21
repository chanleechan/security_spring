package com.project.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Key;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecretKey {
    private Key accessSecretKey;
    private Key refreshSecretKey;
}
