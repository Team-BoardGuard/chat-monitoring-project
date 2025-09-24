package com.guard.communityservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Spring Security 설정을 활성화합니다.
public class SecurityConfig {
	// 👇 이 부분이 PasswordEncoder를 Bean으로 등록하는 핵심 코드입니다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt는 현재 가장 널리 사용되는 안전한 해싱 알고리즘입니다.
        return new BCryptPasswordEncoder();
    }
    
 // (추가) Spring Security 6.1 이상부터는 아래 설정을 통해
    // 회원가입, 로그인 없이 모든 페이지에 접근할 수 있도록 임시로 허용할 수 있습니다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (개발 편의를 위해)
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/**").permitAll() // 모든 경로에 대해 접근 허용
                .anyRequest().authenticated()
            );
        return http.build();
    }
}
