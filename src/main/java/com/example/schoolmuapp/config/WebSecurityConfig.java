package com.example.schoolmuapp.config;

import com.example.schoolmuapp.serviceImpl.UserServiceImpl;
import com.example.schoolmuapp.utils.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private UserServiceImpl userService;
    private JwtAuthenticationFilter authentication;

    @Autowired
    public WebSecurityConfig(@Lazy UserServiceImpl userService, JwtAuthenticationFilter authentication){
        this.userService = userService;
        this.authentication = authentication;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder();}

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(username -> userService.loadUserByUsername(username));
        //daoAuthenticationProvider.setUserDetailsService(username -> userService.loadUserByUsername(username));
        return daoAuthenticationProvider;
    }
    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(httpRequest ->
                        httpRequest
                                .requestMatchers("/api/v1/sign-up", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/api/v1/google/**", "/v3/api-docs.yaml",
                                        "/api/v1/login").permitAll()
                                .requestMatchers("/api/v1/index", "/api/v1/post/createPost", "/api/v1/buyBook/rentBook", "/api/resetPassword").authenticated())
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authentication, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}



