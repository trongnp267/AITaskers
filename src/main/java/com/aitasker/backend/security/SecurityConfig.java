package com.aitasker.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired private JwtAuthenticationFilter jwtAuthenticationFilter;

    // TRUOC DAY: JwtAuthenticationFilter co @Component, nen Spring Boot TU DONG
    // dang ky no THEM MOT LAN NUA nhu mot servlet filter thuong ap dung cho
    // "/*", nam NGOAI chuoi bao mat cua Spring Security (chuoi nay chi la MOT
    // filter duy nhat - DelegatingFilterProxy - trong toan bo chuoi filter cua
    // servlet container). Lan chay "ngoai luong" nay lam SecurityContext bi
    // thiet lap roi bi ghi de/reset truoc khi request thuc su di vao chuoi bao
    // mat, nen du token gui len HOAN TOAN HOP LE, he thong van coi nhu chua
    // dang nhap -> tra ve 403. Vo hieu hoa dang ky tu dong nay, CHI giu lai
    // duy nhat 1 lan dang ky dung cach ben trong SecurityFilterChain o duoi.
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> disableAutoRegistration(
            JwtAuthenticationFilter filter
    ) {
        FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/login", "/api/register",
                    "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
                    "/h2-console/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
