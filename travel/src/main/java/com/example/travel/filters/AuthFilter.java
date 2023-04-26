package com.example.travel.filters;

import com.example.travel.Util.JWTUtil;
import com.example.travel.dto.CustomUserDetails;
import com.example.travel.exception.NotAuthenticatedException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Log4j2
public class AuthFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException {
        log.info("Filtering request, {} with HttpMethod, {}", request.getRequestURI(), request.getMethod());

        Optional<String> authHeader = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION));

        if(authHeader.isPresent()) {
            String token = getToken(authHeader.get());
            Map<String, Object> claims = jwtUtil.parseClaims(token);

            SecurityContextHolder.getContext().setAuthentication(createAuthentication(token, claims));
            filterChain.doFilter(request, response);
        }else {
            log.warn("Incoming request with missing authorization header, Fiter action : false");
            filterChain.doFilter(request, response);
        }
    }

    private String getToken(String authHeader) {
        log.info("Retrieving tokens from header");

        return Optional.of(authHeader)
                .filter(auth -> auth.startsWith("Bearer"))
                .map(auth -> auth.replace("Bearer", "").trim())
                .orElseThrow(() -> new NotAuthenticatedException("Token format is invalid"));
    }

    private Authentication createAuthentication(String token, Map<String, Object> claims) {

        try {
            CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(claims.get(Claims.SUBJECT).toString());
            boolean isTokenValid = jwtUtil.validateToken(token, customUserDetails);
            int userId = (int) claims.get("userId");

            log.info("Token valid status : {}", isTokenValid);
            log.info(customUserDetails.getAuthorities());

            if (isTokenValid && userId == customUserDetails.getId()) {
                return new UsernamePasswordAuthenticationToken(customUserDetails.getId(), null, customUserDetails.getAuthorities());
            } else {
                throw new NotAuthenticatedException("Request token is invalid.");
            }

        }catch (BadCredentialsException exception) { // This exception is likely to occur if cannot find user by username using token's subject
            throw new NotAuthenticatedException("Request token is invalid");
        }


    }

}
