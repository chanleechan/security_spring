package com.project.security.jwt.component;

import com.project.security.jwt.dto.Token;
import com.project.user.domain.JwtRefreshToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret}")
    private static String secret;
    @Value("${jwt.refreshSecret}")
    private static String refreshSecret;

    @Value("${jwt.expiration}")
    private static long tokenValidityInMilliseconds;

    //Key 생성
    public Key setKey(String secret) {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //토큰을 이용해 loginId 추출
    public String tokenGetLoginId(String token, String secretKey) {
        return getClaims(token, secretKey).get("loginId").toString();
    }

    //클레임 추출
    public Claims getClaims(String token, String secretKey) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    //토큰 만료시간 체크
    public boolean tokenIsExpired(String token, String secretKey) {
        Date expiredDate = getClaims(token, secretKey).getExpiration();
        // Token의 만료 날짜가 지금보다 이전인지 check
        return expiredDate.before(new Date());
    }

    //토큰 생성 accessToken , refreshToken
    public Token createToken(String loginId) {
        Claims claims = Jwts.claims();
        claims.put("loginId", loginId);
        claims.put("roles", "member");

        String accessToken = createAccessToken(claims, tokenValidityInMilliseconds);
        String refreshToken = createRefreshToken(claims, tokenValidityInMilliseconds);

        return Token.builder().accessToken(accessToken).refreshToken(refreshToken).key(loginId).build();
    }

    //엑세스 토큰 생성
    public String createAccessToken(Claims claims, long tokenValidityInMilliseconds) {
        claims.put("type", "access");
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidityInMilliseconds))
                .signWith(setKey(secret), SignatureAlgorithm.HS512)
                .compact();
    }

    //리프레쉬 토큰 생성
    public String createRefreshToken(Claims claims, long tokenValidityInMilliseconds) {
        claims.put("type", "refresh");
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidityInMilliseconds))
                .signWith(setKey(refreshSecret), SignatureAlgorithm.HS512)
                .compact();
    }

    //리프레쉬 토큰 검증
    public String validateRefreshToken(JwtRefreshToken refresh) {

        String refreshToken = refresh.getRefreshToken();
        try {
            Claims claims = getClaims(refreshToken, refreshSecret);
            if (!claims.getExpiration().before(new Date())) {
                return recreateAccessToken(claims.get("loginId").toString());
            }
        } catch (ExpiredJwtException e) {
            return "";
        }
        return "";
    }

    public String recreateAccessToken(String loginId) {
        Claims claims = Jwts.claims().setSubject(loginId);
        return createAccessToken(claims, tokenValidityInMilliseconds);
    }
}
