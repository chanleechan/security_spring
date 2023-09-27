package com.project.security.jwt.component;

import com.project.redis.domain.RefreshToken;
import com.project.redis.domain.RefreshTokenRepository;
import com.project.security.jwt.dto.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.refreshSecret}")
    private String refreshSecret;

    @Value("${jwt.expiration}")
    private long tokenValidityInMilliseconds;
    private final RefreshTokenRepository refreshTokenRepository;

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
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw e;
        }
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
        refreshTokenRepository.save(RefreshToken.create(refreshToken, loginId));

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

    //리프레쉬 토큰 삭제
    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
    }

    public String validateRedisRefreshToken(RefreshToken refresh) {

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
        String token = loginId + ":" + createAccessToken(claims, tokenValidityInMilliseconds);
        refreshTokenRepository.save(RefreshToken.create(token, loginId));
        return token;
    }

    public void redisRefreshTokenSaveOrUpdate(String refreshToken, String loginId) {
        refreshTokenRepository.findById(refreshToken)
                .ifPresentOrElse(
                        redisRefreshToken -> {
                            redisRefreshToken.update(refreshToken);
                            refreshTokenRepository.save(redisRefreshToken);
                        },
                        () -> {
                            refreshTokenRepository.save(RefreshToken.create(refreshToken, loginId));
                        }
                );
    }

    public ResponseCookie setResponseCookie(String cookieName, String value, long maxAge) {
        return ResponseCookie.from(cookieName, value)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(maxAge)
                .build();
    }


}
