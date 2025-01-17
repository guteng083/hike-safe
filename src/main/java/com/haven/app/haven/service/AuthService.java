package com.haven.app.haven.service;

import com.haven.app.haven.dto.request.LoginRequest;
import com.haven.app.haven.dto.request.RegisterRequest;
import com.haven.app.haven.dto.response.LoginResponse;

public interface AuthService {
    void registerAdmin(RegisterRequest request, String secretKey);
    void registerStaff(RegisterRequest request);
    void registerCustomer(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
