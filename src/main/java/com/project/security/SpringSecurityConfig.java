package com.project.security;

import com.project.security.jwt.component.AccessDeniedHandlerCustom;
import com.project.security.jwt.component.AuthenticationEntryPointHandlerCustom;
import com.project.security.jwt.component.JwtFilter;
import com.project.security.jwt.component.JwtUtil;
import com.project.user.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SpringSecurityConfig {
    private final SecurityService userService;
    private final AccessDeniedHandlerCustom accessDeniedHandlerCustom;
    private final AuthenticationEntryPointHandlerCustom authenticationEntryPointHandlerCustom;
    private final JwtUtil util;

    @Value("${jwt.secret}")
    String secretKey;

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //.csrf().disable().cors().disable() -> 실제 운영환경 에서는 삭제 고려
        // JWT 토큰 사용할 경우
        http.httpBasic().disable().csrf().disable()
                //세션 사용 안함
                //유저 패스워드 필터 접근 전 Jwt 필터로 접근
                .addFilterBefore(new JwtFilter(secretKey, userService, util), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/", "/images/**", "/user/login", "/user/logout", "/login/join", "/login/user", "/user/join", "/js/**", "/token/**", "/favicon")
                            .permitAll()
                            .anyRequest().authenticated();
                })
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandlerCustom)
                .authenticationEntryPoint(authenticationEntryPointHandlerCustom);
        return http.build();
    }

}
