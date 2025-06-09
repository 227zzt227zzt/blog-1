package com.zzt.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Spring Security配置
 * @author 227
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(authorize -> authorize
                    // 允许访问静态资源（例如图片）
                    .requestMatchers("/upload/**").permitAll()
                    // 允许访问Swagger/Knife4j相关资源
                    .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/doc.html", "/webjars/**").permitAll()
                    // 允许登录和注册接口
                    .requestMatchers("/users/login", "/users/register","/captcha/**").permitAll()
                    // 允许AI相关接口
                    .requestMatchers("/ai/**").authenticated()
                    // 允许管理员访问 /admin/** 接口
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    // 其他请求需要认证
                    .anyRequest().authenticated()
            )
            // 禁用CSRF保护，简化API访问
            .csrf(AbstractHttpConfigurer::disable)
            // 使用无状态会话，不需要服务器存储会话状态
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 添加JWT过滤器
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            // 配置异步请求支持
            .securityContext(context -> context.requireExplicitSave(false))
            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives("frame-ancestors 'self'")
                )
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8081", "http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}