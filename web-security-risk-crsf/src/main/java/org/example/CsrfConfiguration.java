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

    // TODO. 默认激活CSRF防护, 避免(浏览器+Cookie认证)带来的安全问题
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
        // 直接关闭所有CSRF防护, 可能带来安全提示/影响API操作测试
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        // 纯无状态REST API(且不使用Cookie认证)可能不需要CSRF防护
        // Allows specifying HttpServletRequest that should not use CSRF Protection
        httpSecurity.csrf(csrf -> csrf.ignoringRequestMatchers("/**"));

        return httpSecurity.build();
    }
}
