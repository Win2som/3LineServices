package com.example.transaction.utils;

import com.example.transaction.dto.Content;
import com.example.transaction.dto.User;
import com.example.transaction.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final RestTemplate restTemplate;
    private final JwtUtils utils;

    @Value("${read-user-url}")
    private String readUrl;

    @Value("${update-user-url}")
    private String putUrl;

    @Value("${read-content-url}")
    private String readContentUrl;

    public User readUser(String param, HttpServletRequest request){

        String finalUrl = readUrl + param;

        HttpEntity<HttpHeaders> jwtEntity = new HttpEntity<>(getHeaders(request));

        ResponseEntity<User> user = restTemplate.exchange(finalUrl, HttpMethod.GET, jwtEntity,
                User.class);

        return (user.getStatusCode() == HttpStatus.OK) ? user.getBody() : null;
    }



    public void updateAccount(User user, HttpServletRequest request){

        HttpEntity<User> jwtEntity = new HttpEntity<>(user, getHeaders(request));

        String finalPutUrl = putUrl + user.getId();
        restTemplate.exchange(finalPutUrl, HttpMethod.PUT, jwtEntity, Void.class, user.getId());
    }


    public Content readContent(String title, HttpServletRequest request) {
        String newUrl = readContentUrl + title;

        HttpEntity<HttpHeaders> jwtEntity = new HttpEntity<>(getHeaders(request));

        ResponseEntity<Content> content = restTemplate.exchange(newUrl, HttpMethod.GET, jwtEntity,
                Content.class);

        return (content.getStatusCode() == HttpStatus.OK) ? content.getBody() : null;
    }

    private HttpHeaders getHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Authorization", "Bearer " + utils.getJWTFromRequest(request));
        return headers;
    }
}
