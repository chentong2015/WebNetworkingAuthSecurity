package org.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class CsrfConfiguration {

    // TODO. 关闭CSRF保护后有利于Postman测试
    // 纯无状态REST API(且不使用Cookie认证)可能不需要CSRF防护
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
        // Allows specifying HttpServletRequest that should not use CSRF Protection
        httpSecurity.csrf(csrf -> csrf.ignoringRequestMatchers("/**"));

        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}
