package com.goorm.profileboxapiadmin.auth;


import com.goorm.profileboxcomm.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private CorsFilter corsFilter;
    private AuthenticationConfiguration authenticationConfiguration;
    private MemberRepository memberRepository;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 면접에서 물어볼만한 질문: 인증방식 중에 하나를 선택햇 ㅓ jwt를 선택한건데 왜 세션을 사용하냐?. 왜 jwt를 사용했는지?

        http.httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // csrf

        http.authorizeHttpRequests()
                .requestMatchers("/v1/auth/member/login", "/v1/auth/member/register").permitAll()
//                .requestMatchers("api/v1/notice/admin/**").hasRole("ADMIN") // 추후에 usertype을 나누는게 확실하다면 이 주석 풀기.
//                .anyRequest().permitAll();
                .anyRequest().authenticated()
                .and()
                .addFilter(corsFilter)
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
//                .addFilterBefore(new JwtAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), memberRepository))
                .formLogin().disable();

        return http.build();
    }

//    loginProcessingUrl

}
