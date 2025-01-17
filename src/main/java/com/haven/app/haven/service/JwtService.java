package com.haven.app.haven.service;

import com.haven.app.haven.entity.Users;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(Users users);
    String extractEmail(String jwtToken);
    boolean validateToken(String jwtToken, UserDetails users);
}
