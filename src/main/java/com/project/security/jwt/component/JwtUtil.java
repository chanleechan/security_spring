package com.project.security.jwt.component;

import com.project.security.jwt.dto.Token;
import com.project.user.domain.JwtRefreshToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.refreshSecret}")
    private String refreshSecret;

    @Value("${jwt.expiration}")
    private long tokenValidityInMilliseconds;

    public Key setKey(String secret) {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static String tokenGetLoginId(String token, String secretKey) {
        return extractClaims(token, secretKey).get("loginId").toString();
    }

    private static Claims extractClaims(String token, String secretKey) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    public static boolean isExpired(String token, String secretKey) {
        Date expiredDate = extractClaims(token, secretKey).getExpiration();
        // Token의 만료 날짜가 지금보다 이전인지 check
        return expiredDate.before(new Date());
    }

    public Token createToken(String loginId) {
        // Claim = Jwt Token에 들어갈 정보
        // Claim에 loginId를 넣어 줌으로써 나중에 loginId를 꺼낼 수 있음
        Claims claims = Jwts.claims();
        claims.put("loginId", loginId);

        String accessToken = createAccessToken(claims, tokenValidityInMilliseconds);
        String refreshToken = createRefreshToken(claims, tokenValidityInMilliseconds);

        return Token.builder().accessToken(accessToken).refreshToken(refreshToken).key(loginId).build();
    }

    private String createAccessToken(Claims claims, long tokenValidityInMilliseconds) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidityInMilliseconds))
                .signWith(setKey(secret), SignatureAlgorithm.HS512)
                .compact();
    }

    private String createRefreshToken(Claims claims, long tokenValidityInMilliseconds) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidityInMilliseconds))
                .signWith(setKey(refreshSecret), SignatureAlgorithm.HS512)
                .compact();
    }

    public String validateRefreshToken(JwtRefreshToken refresh) {

        String refreshToken = refresh.getRefreshToken();
        try {
            Claims claims = extractClaims(refreshToken, refreshSecret);
            if (!claims.getExpiration().before(new Date())) {
                return recreateAccessToken(claims.get("loginId").toString());
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    public String recreateAccessToken(String loginId) {
        Claims claims = Jwts.claims().setSubject(loginId);
        return createAccessToken(claims, tokenValidityInMilliseconds);
    }


}
