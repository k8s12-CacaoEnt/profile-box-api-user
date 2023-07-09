package com.goorm.profileboxapiuser.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

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
}
