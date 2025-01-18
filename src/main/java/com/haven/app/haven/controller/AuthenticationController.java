package com.haven.app.haven.controller;

import com.haven.app.haven.constant.Endpoint;
import com.haven.app.haven.dto.request.ChangePasswordRequest;
import com.haven.app.haven.dto.request.LoginRequest;
import com.haven.app.haven.dto.request.RegisterRequest;
import com.haven.app.haven.dto.response.CommonResponse;
import com.haven.app.haven.dto.response.CommonResponseWithData;
import com.haven.app.haven.dto.response.LoginResponse;
import com.haven.app.haven.dto.response.RegisterResponse;
import com.haven.app.haven.service.AuthService;
import com.haven.app.haven.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoint.API_AUTH)
public class AuthenticationController {
    private final AuthService authService;

    @PostMapping(path = "/register-customer")
    public CommonResponse RegisterCustomer(@RequestBody RegisterRequest registerRequest) {
        authService.registerCustomer(registerRequest);
        return ResponseUtils.Response("Register Success");
    }

    @PostMapping(path = "/register-admin")
    public CommonResponse RegisterAdmin(
            @RequestBody RegisterRequest registerRequest,
            @RequestHeader(name = "X-ADMIN-SECRET-KEY") String secretKey) {
        authService.registerAdmin(registerRequest, secretKey);
        return ResponseUtils.Response("Register Success");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/register-staff")
    public CommonResponse RegisterStaff(@RequestBody RegisterRequest registerRequest) {
        authService.registerStaff(registerRequest);
        return ResponseUtils.Response("Register Success");
    }

    @PostMapping(path = "/login")
    public CommonResponseWithData<LoginResponse> Login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseUtils.ResponseWithData("Success", response);
    }

    @PatchMapping(path = "/password/update")
    public CommonResponse changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        authService.changePassword(changePasswordRequest);
        return ResponseUtils.Response("Change Password Success");
    }

    @GetMapping(path = "/me")
    public CommonResponseWithData<LoginResponse> me() {
        LoginResponse getMe = authService.getMe();
        return ResponseUtils.ResponseWithData("Success", getMe);
    }
}
