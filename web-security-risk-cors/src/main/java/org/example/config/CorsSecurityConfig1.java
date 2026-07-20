package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class CorsSecurityConfig1 {

    // TODO. 彻底禁用CORS安全策略: 允许任意的前端JS代码访问该域的API
    // 禁用CORS后，虽然方便开发，但也允许了恶意网站的访问
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
        return httpSecurity.cors(AbstractHttpConfigurer::disable)
                .build();
    }
}
