package com.goorm.profileboxapiuser.auth;


import com.goorm.profileboxapiuser.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final MemberService memberService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final String[] allowedUrls = {"/", "/swagger-ui/**", "/v3/**"};

    public AuthenticationManager authenticationManager() throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.httpBasic().disable()
                .csrf().disable()
                .cors()
                .and()
                .addFilter(corsFilter)
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), memberService))
                .authorizeHttpRequests()
//                .anyRequest().permitAll()
                // hasRole이나 hasAnyRole은 "ROLE_" prefix를 붙여버림.
//                .requestMatchers("v1/notice/admin/**").hasAnyAuthority("ADMIN", "PRODUCER")
                //.requestMatchers("v1/notice/admin/**").hasAuthority("ADMIN")
                .requestMatchers(allowedUrls).permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // csrf
                .and()
                .build();
    }

}
