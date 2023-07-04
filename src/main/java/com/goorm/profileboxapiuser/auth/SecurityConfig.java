package com.goorm.profileboxapiuser.auth;


import com.goorm.profileboxapiuser.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final MemberService memberService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final String[] allowedUrls = {"/"
                            , "/swagger-ui/**"
                            , "/swagger-resources/**"
                            , "/v3/api-docs/**"
                            , "/api/user/swagger-ui/**"
                            , "/api/user/swagger-resources/**"
                            , "/api/user/v3/api-docs/**"
    };

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
                .requestMatchers(allowedUrls).permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // csrf
                .and()
                .build();
    }

}
