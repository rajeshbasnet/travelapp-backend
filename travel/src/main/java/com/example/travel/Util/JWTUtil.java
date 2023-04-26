package com.example.travel.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;


@Component
@Log4j2
@PropertySource(value = "classpath:application.properties")
public class JWTUtil{

    private final Key key;
    @Value("${token.expiration}")
    private Long expiration;

    public JWTUtil(@Value("${jwt.secret.key}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String subject, Map<String, Object> claims) {
        return Jwts
                .builder()
                .setSubject(subject)
                .addClaims(claims) // SetClaims won't add subject into token
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public Map<String, Object> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        Map<String, Object> claims = parseClaims(token);
        String username = (String) claims.get(Claims.SUBJECT);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Map<String, Object> claims = parseClaims(token);
        Function<Claims, Date> claimResolver = Claims::getExpiration;
        boolean isTokenExpired = claimResolver.apply((Claims) claims).before(new Date());
        log.info("Token expired : {}", isTokenExpired);
        return isTokenExpired;
    }


}
