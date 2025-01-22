package com.haven.app.haven.controller;

import com.haven.app.haven.constant.Endpoint;
import com.haven.app.haven.dto.request.ChangePasswordRequest;
import com.haven.app.haven.dto.request.LoginRequest;
import com.haven.app.haven.dto.request.RegisterRequest;
import com.haven.app.haven.dto.response.CommonResponse;
import com.haven.app.haven.dto.response.CommonResponseWithData;
import com.haven.app.haven.dto.response.LoginResponse;
import com.haven.app.haven.service.AuthService;
import com.haven.app.haven.utils.ResponseUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoint.API_AUTH)
@Validated
@Tag(name="Authentication", description = "APIs for authentication")
public class AuthenticationController {
    private final AuthService authService;

    @SecurityRequirements
    @PostMapping(path = "/register-customer")
    public CommonResponse RegisterCustomer(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.registerCustomer(registerRequest);
        return ResponseUtils.response("Register Success");
    }

    @SecurityRequirements
    @PostMapping(path = "/register-admin")
    public CommonResponse RegisterAdmin(
            @Valid
            @RequestBody RegisterRequest registerRequest,

            @Valid
            @NotNull(message = "Admin Secret Key is Required")
            @RequestHeader(name = "X-ADMIN-SECRET-KEY") String secretKey) {
        authService.registerAdmin(registerRequest, secretKey);
        return ResponseUtils.response("Register Success");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/register-staff")
    public CommonResponse RegisterStaff(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.registerStaff(registerRequest);
        return ResponseUtils.response("Register Success");
    }

    @SecurityRequirements
    @PostMapping(path = "/login")
    public CommonResponseWithData<LoginResponse> Login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseUtils.responseWithData("Success", response);
    }

    @PatchMapping(path = "/password/update")
    public CommonResponse changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        authService.changePassword(changePasswordRequest);
        return ResponseUtils.response("Change Password Success");
    }

    @GetMapping(path = "/me")
    public CommonResponseWithData<LoginResponse> me() {
        LoginResponse getMe = authService.getMe();
        return ResponseUtils.responseWithData("Success", getMe);
    }
}
