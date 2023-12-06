package com.example.api.services.Auth;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.example.api.models.User;
import com.example.api.repositories.UserRepository;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {
    private final String secret_key = "change_this_secret_key"; // TODO: change this secret key
    private long accessTokenValidity = 60;

    private final JwtParser jwtParser;
    private final UserRepository userRepository;

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    public JwtUtil(UserRepository userRepository) {
        this.jwtParser = Jwts.parser().setSigningKey(secret_key);
        this.userRepository = userRepository;
    }

    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("firstName",user.getFirstName());
        claims.put("lastName",user.getLastName());
        
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
        
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);

            if (token != null) {
                return parseJwtClaims(token);
            }

            return null;
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(TOKEN_HEADER);

        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }

        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            throw e;
        }
    }

    public String getAuthUsername(String token) {
        Claims claims = parseJwtClaims(token);

        return claims.getSubject();
    }

    public Integer getAuthUserId(String authorization) {
        String token = authorization.replace("Bearer ", "");

        String username = getAuthUsername(token);

        User user = userRepository.findByUsername(username);

        return user.getId();
    }

    public User getAuthUser(String token) {
        String username = getAuthUsername(token);

        if (username == null) {
            throw new RuntimeException("Username not found");
        }

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return user;
    }
}
