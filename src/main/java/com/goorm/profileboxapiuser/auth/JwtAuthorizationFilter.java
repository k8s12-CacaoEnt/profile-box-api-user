package com.goorm.profileboxapiadmin.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.goorm.profileboxcomm.entity.Member;
import com.goorm.profileboxcomm.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private MemberRepository memberRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("authorization 요청");
        String jwtHeader = request.getHeader("Authorization");
        // if 조건문 코드 한줄로 딱 하는거 찾아보기
        if(jwtHeader == null || !jwtHeader.startsWith("Bearer")){
            chain.doFilter(request, response);
            return; // status 코드 확인 필요.
        }
        String clientJwtToken = request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX,"");
        String email = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(clientJwtToken).getClaim("email").asString();

        if(email != null){
            System.out.println("authorization - jwt토큰을 가진 사용자입니다.");
            Member memberEntity = memberRepository.findMemberByMemberEmail(email);
            PrincipalDetails princialDetails = new PrincipalDetails(memberEntity);
            // jwt 토큰 서명을 통해서 서명이 정상이면 authentication 객체를 만들어 준다. -> 서명을 통해 username이 있으면 authentication을 만들어 준다.
            Authentication authentication = new UsernamePasswordAuthenticationToken(princialDetails, null, princialDetails.getAuthorities());
            // 강제로 시큐리티의 세션에 접근하여 authentication 객체를 저장한다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }

    }
}
