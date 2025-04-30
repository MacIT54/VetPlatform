package com.vet.auth_service.security.jwt;

import com.vet.auth_service.model.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Getter
    @Value("${jwt.expiration}")
    private long validityInMilliseconds;

    public String createToken(AuthUser authUser) {
        Claims claims = Jwts.claims().setSubject(authUser.getLogin());
        claims.put("id", String.valueOf(authUser.getId()));
        claims.put("role", authUser.getUserType().name());
        claims.put("name", authUser.getName());
        claims.put("surname", authUser.getSurname());
        claims.put("email", authUser.getEmail());
        claims.put("enabled", authUser.isEnabled());

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public String getRole(String token) {
        return (String) getClaims(token).get("role");
    }

    public String getName(String token) {
        return (String) getClaims(token).get("name");
    }

    public String getSurname(String token) {
        return (String) getClaims(token).get("surname");
    }

    public String getEmail(String token) {
        return (String) getClaims(token).get("email");
    }

    public boolean getEnabled(String token) {
        return (boolean) getClaims(token).get("enabled");
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}

