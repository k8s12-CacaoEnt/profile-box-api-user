package com.goorm.profileboxapiuser.service;

import com.goorm.profileboxapiuser.auth.JwtProperties;
import com.goorm.profileboxcomm.entity.Member;
import com.goorm.profileboxcomm.exception.ApiException;
import com.goorm.profileboxcomm.exception.ExceptionEnum;
import com.goorm.profileboxcomm.response.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class MemberService {

//    private final ProducerService producerService;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RestTemplate restTemplate;
    private static final String TOPIC_NAME = "user-authenticated";

    @Value("${server.host.adminApi}")
    private String adminApiUrl;

    // admin api로 요청해야함
    public Member findLoginMemberByEmail(String email, String clientJwtToken) throws ExecutionException, InterruptedException {
        String url = adminApiUrl;
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.COOKIE, JwtProperties.ACCESS_TOKEN_COOKIE + "=" + clientJwtToken);


        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        URI uri = UriComponentsBuilder
                .fromUriString(url)
                .path("/v1/member/email/{email}")
                .encode()
                .build()
                .expand(email)
                .toUri();

        try{
            ResponseEntity<ApiResult<Member>> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<ApiResult<Member>>() {});
            return responseEntity.getBody().getData();
        }catch (Exception e){
            throw new ApiException(ExceptionEnum.NOT_RESPONSE_ADMIN_API);
        }
    }
}
