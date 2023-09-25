package com.project.security.jwt.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.user.domain.SecurityUser;
import com.project.user.dto.ApiResponse;
import com.project.user.service.SecurityService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final String key;
    private final SecurityService userService;
    private final JwtUtil util;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        //refresh token 일 경우
        if (servletPath.equals("/token/refresh")) {
            filterChain.doFilter(request, response);
        } else if (servletPath.equals("/user/logout")) {
            //TODO 로그아웃 관련 blackList 추가해야함
        } else {
            try {
                //토큰 가져오기
                //header에 없을 시 쿠키에서 토큰 찾아서 가져오기
                String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
                if (authorizationHeader == null) {
                    if (request.getCookies() == null) {
                        filterChain.doFilter(request, response);
                        return;
                    }
                    String accessToken = requestCookieToString(getTokenCookie(request, "accessToken"), request, response, filterChain);
                    if (accessToken.isEmpty()) return;
                    authorizationHeader = "Bearer " + accessToken;
                }
                String token = authorizationHeader.replace("Bearer ", "");
                if (util.tokenIsExpired(token, key)) {
                    filterChain.doFilter(request, response);
                    return;
                }
                setSecurityContext(token, request);
                filterChain.doFilter(request, response);
                //access 토큰 만료 exception
            } catch (ExpiredJwtException ex) {
                setResponseMsg(response, SC_UNAUTHORIZED, MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getWriter(), ApiResponse.create("expire", "Access 토큰이 만료됨"));
                //access 토큰 오류 exception
            } catch (Exception e) {
                setResponseMsg(response, SC_BAD_REQUEST, MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getWriter(), ApiResponse.create("400", "잘못된 JWT Token"));
            }
        }
    }


    //Security Context에 사용자 정보 저장
    private void setSecurityContext(String token, HttpServletRequest request) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        String loginId = util.tokenGetLoginId(token, key);
        SecurityUser loginUser = userService.loadUserByUsername(loginId);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.setContext(context);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    //쿠키로 토큰 Bearer 형식으로 Parsing
    private String requestCookieToString(Cookie cookie, HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (cookie == null) {
            filterChain.doFilter(request, response);
            return "";
        }
        String accessToken = cookie.getValue();
        return "Bearer " + accessToken;
    }

    private Cookie getTokenCookie(HttpServletRequest request, String cookieName) {
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(cookieName))
                .findFirst()
                .orElse(null);
    }

    private void setResponseMsg(HttpServletResponse response, int status, String contentType) {
        response.setStatus(status);
        response.setContentType(contentType);
        response.setCharacterEncoding("utf-8");
    }

}
