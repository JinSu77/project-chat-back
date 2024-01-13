package com.example.api.services.Auth;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.example.api.models.User;
import com.example.api.repositories.UserRepository;

import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {
    @Value("${application.jwt.secret-key}")
    private String secret_key;

    @Value("${application.mercure.subscriber.jwt-key}")
    private String mercureSubscriberJwtKey;

    private long accessTokenValidity = 60;

    private final JwtParser jwtParser;
    private final UserRepository userRepository;

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    public JwtUtil(UserRepository userRepository, @Value("${application.jwt.secret-key}") String secret_key) {
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

    public String createMercureToken(User user) {
        Claims claims = Jwts.claims();
        claims.put("mercure", Map.of(
                "subscribe", new String[] { 
                    "/channels/{id}",
                    "/conversations/{id}",
                    "/users/" + user.getUsername() + user.getId().toString() + "/contacts",

                },
                "publish", new String[] {
                    "*"
                }
        ));

        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
        String secretKeyBase64 = Base64.getEncoder().encodeToString(mercureSubscriberJwtKey.getBytes());

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secretKeyBase64)
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
