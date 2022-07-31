package com.example.transaction.security;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtTokenAuthenticationInterceptor implements HandlerInterceptor {

        private final JwtUtils jwtUtils;

        public JwtTokenAuthenticationInterceptor(JwtUtils jwtUtils) {
            this.jwtUtils = jwtUtils;
        }

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            try {
                String jwt = jwtUtils.getJWTFromRequest(request);
                if (StringUtils.hasText(jwt) && jwtUtils.validateJwtToken(jwt)) {
                    String subject = jwtUtils.getSubjectFromJwtToken(jwt);
                    if (subject == null) throw new RuntimeException("Invalid bearer token");
                }
                return true;
            } catch (Exception exception) {
                throw new RuntimeException(exception.getMessage());
            }
        }

}
