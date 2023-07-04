package com.goorm.profileboxapiuser.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.goorm.profileboxapiuser.service.MemberService;
import com.goorm.profileboxcomm.entity.Member;
import com.goorm.profileboxcomm.exception.ApiException;
import com.goorm.profileboxcomm.exception.ExceptionEnum;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final MemberService memberService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,MemberService memberService) {
        super(authenticationManager);
        this.memberService = memberService;
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);

        if(jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)){
//            throw new ApiException(ExceptionEnum.TOKEN_NOT_FOUND);
            chain.doFilter(request, response);
            return;
        }

        String clientJwtToken = request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX,"");
        String email = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(clientJwtToken).getClaim("email").asString();
        // throw new ApiException(ExceptionEnum.EXFIRED_TOKEN);

        if(email != null){
            Member member = memberService.findLoginMemberByEmail(email, clientJwtToken);
            PrincipalDetails princialDetails = new PrincipalDetails(member);
            Authentication authentication = new UsernamePasswordAuthenticationToken(princialDetails, null, princialDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }else{
            throw new ApiException(ExceptionEnum.SECURITY);
        }
    }
}
