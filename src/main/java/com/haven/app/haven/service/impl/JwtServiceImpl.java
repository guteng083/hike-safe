package com.haven.app.haven.service.impl;

import com.haven.app.haven.entity.Users;
import com.haven.app.haven.exception.AuthenticationException;
import com.haven.app.haven.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret_key}")
    protected String secretKey;
    @Override
    public String generateToken(Users users) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", users.getId());
        claims.put("roles", users.getRole());
        return Jwts.builder()
                .subject(users.getEmail())
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() +(3600*24*7*1000)))
                .signWith(getKey())
                .compact();
    }

    @Override
    public String extractEmail(String jwtToken) {
        try {
            return extractClaim(jwtToken, Claims::getSubject);
        } catch (ExpiredJwtException e) {
            throw new AuthenticationException("JWT Token expired");
        } catch (JwtException e) {
            throw new AuthenticationException("JWT Token invalid");
        }
    }

    @Override
    public boolean validateToken(String jwtToken, UserDetails users) {
        final String username = extractEmail(jwtToken);
        if (isTokenExpired(jwtToken)) {
            throw new AuthenticationException("JWT Token expired");
        }
        return (username.equals(users.getUsername()));
    }

    public <T> T extractClaim(String jwtToken, Function<Claims,T> claimResolver) {
        final Claims claims = extractALLClaims(jwtToken);
        return claimResolver.apply(claims);
    }

    private Claims extractALLClaims(String jwtToken) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    private boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
