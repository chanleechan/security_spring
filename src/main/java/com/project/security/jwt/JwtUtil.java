package com.project.security.jwt;

import com.project.security.jwt.dto.Token;
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

    @Value("${jwt.expiration}")
    private long tokenValidityInMilliseconds;

    public Key setKey() {
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

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidityInMilliseconds))
                .signWith(setKey(), SignatureAlgorithm.HS512)
                .compact();
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidityInMilliseconds))
                .signWith(setKey(), SignatureAlgorithm.HS512)
                .compact();

        return Token.builder().accessToken(accessToken).refreshToken(refreshToken).key(loginId).build();
    }

    /*public Authentication getAuthentication(String token) {
        UserDetails user = userDetailsService.loadUserByUsername(tokenGetLoginId(token, secret));

    }*/


}
