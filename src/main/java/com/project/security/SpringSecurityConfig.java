package com.project.security;

import com.project.security.jwt.JwtFilter;
import com.project.security.jwt.JwtUtil;
import com.project.user.service.SecurityService;
import com.project.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SpringSecurityConfig {
    private final SecurityService userService;
    private final UserService service;
    private final AuthenticationFailureHandlerCustom failureHandler;
    private final AuthenticationSuccessHandlerCustom successHandler;
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

       /* 시큐리티만 사용할 경우
       http.csrf().disable().cors().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .requestMatchers("/images/**", "/user/login", "/login/join", "/user/join", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/user/login")
                        .usernameParameter("userId")
                        .passwordParameter("pw")
                        .loginProcessingUrl("/login/user")
                        .failureHandler(failureHandler)
                        .successHandler(successHandler)
                        *//*.defaultSuccessUrl("/user/userInfo", false)*//*
                        .permitAll()
                )
                .logout(Customizer.withDefaults());
                */

        // JWT 토큰 사용할 경우
        http.httpBasic().disable().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new JwtFilter(secretKey, service), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests().requestMatchers("/login/user").authenticated()
                .requestMatchers("/", "/images/**", "/user/login", "/login/join", "/user/join", "/js/**").permitAll();

        return http.build();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

}
