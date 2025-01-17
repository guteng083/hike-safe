package com.haven.app.haven.service.impl;

import com.haven.app.haven.entity.Users;
import com.haven.app.haven.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    private final String secretKey = "fasghdjkashdjkhsajkdhasjkbcjaskdjkasndkjashd";
    @Override
    public String generateToken(Users users) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", users.getId());
        claims.put("roles", users.getRole());
        return Jwts.builder()
                .subject(users.getEmail())
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() +(60*15*1000)))
                .signWith(getKey())
                .compact();
    }

    @Override
    public String extractEmail(String jwtToken) {
       return extractClaim(jwtToken, Claims::getSubject);
    }

    @Override
    public boolean validateToken(String jwtToken, UserDetails users) {
        final String username = extractEmail(jwtToken);
        return (username.equals(users.getUsername()) && !isTokenExpired(jwtToken));
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
