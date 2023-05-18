package com.DateBuzz.Backend.configuration;

import com.DateBuzz.Backend.configuration.filter.JwtTokenFilter;
import com.DateBuzz.Backend.exception.CustomAuthenticationEntryPoint;
import com.DateBuzz.Backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableWebMvc
public class AuthenticationConfig implements WebMvcConfigurer {

    private final UserService userService;
    @Value("${jwt.secret-key}")
    private String key;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(
                        new AntPathRequestMatcher("/**")).permitAll()
                .requestMatchers(
                        new AntPathRequestMatcher("/join", "POST"),
                        new AntPathRequestMatcher("/login", "POST")
                ).authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JwtTokenFilter(key, userService), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()); // 오류 발생 시 옮겨갈 수 있는 거 -> 이 부분 구현으로 오류 내용 등을 전달 가능;
        return http.build();
    }
    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .allowCredentials(true);
    }

}
