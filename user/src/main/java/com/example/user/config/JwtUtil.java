package com.example.user.config;

import com.example.user.entity.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${secret}")
    private String secret;

    public String generateToken(Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();

            List<String> roles= new ArrayList<>();
            user.getRoles().stream().forEach(r -> roles.add(r.getTitle()));
            return buildJwt(user.getEmail(), user.getId(), roles);
        } catch (Exception e) {
            throw new RuntimeException("Invalid Credentials");
        }
    }

    public String buildJwt(String email, Long id, List<String> roles) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + 1000 * 60 * 60 * 10);

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        claims.put("email", email);
        claims.put("id", id);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    public String getSubjectFromJwtToken(String token) {
        return (String) Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody().get("email");
    }

    public Long getUserIdFromJwtToken(String token) {
        return (Long) Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody().get("id");
    }

    public List<String> getRolesFromJwtToken(String token) {
        return (List<String>) Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody().get("roles");
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

}
