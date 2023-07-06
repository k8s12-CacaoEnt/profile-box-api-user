package com.goorm.profileboxapiuser.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    // JwtToken 생성
    public String createJwtAccessToken(PrincipalDetails principalDetails){
        String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("email", principalDetails.getMemberEntity().getMemberEmail())
                .withClaim("id", principalDetails.getMemberEntity().getMemberId())
                .withClaim("username", principalDetails.getMemberEntity().getMemberName())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
        return jwtToken;
    }

    // Request 쿠키에서 JwtToken 가져오기
    public String getJwtAccessTokenFromCookie(HttpServletRequest request){
        String clientJwtToken = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(JwtProperties.ACCESS_TOKEN_COOKIE)) {
                    clientJwtToken = cookie.getValue();
                    break;
                }
            }
        }
        return clientJwtToken;
    }

    // Response 에 세팅할 JwtToken 쿠키 생성
    public Cookie createJwtAccessTokenCookie(String jwtToken){
        Cookie cookie = new Cookie(JwtProperties.ACCESS_TOKEN_COOKIE, jwtToken);
        cookie.setMaxAge(JwtProperties.EXPIRATION_TIME); //
        cookie.setPath("/"); // 경로 설정
//        cookie.setSecure(true); // Secure 속성 설정
        cookie.setHttpOnly(true); // HttpOnly 속성 설정
        return cookie;
    }
}
